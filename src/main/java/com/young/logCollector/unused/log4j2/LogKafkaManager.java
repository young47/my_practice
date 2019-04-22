package com.young.logCollector.unused.log4j2;


import com.sohu.log.service.LogInfoService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.mom.kafka.DefaultKafkaProducerFactory;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaProducerFactory;
import org.apache.logging.log4j.core.config.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.*;

public class LogKafkaManager extends AbstractManager {
    private static Logger logger = LoggerFactory.getLogger(LogKafkaManager.class);
    public static final String DEFAULT_TIMEOUT_MILLIS = "30000";

    /**
     * package-private access for testing.
     */
    static KafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory();

    private final Properties globalConfig = new Properties();
    private final ConcurrentMap<String, LogProducer> cache = new ConcurrentHashMap<String, LogProducer>(8);
    private final int timeoutMillis;

    private final String topic;

    public LogKafkaManager(final LoggerContext loggerContext, final String name, final Property[] properties) {
        super(loggerContext, name);
        this.topic = null;
        globalConfig.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        globalConfig.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        globalConfig.setProperty("batch.size", "0");
        for (final Property property : properties) {
            globalConfig.setProperty(property.getName(), property.getValue());
        }
        this.timeoutMillis = Integer.parseInt(globalConfig.getProperty("timeout.ms", DEFAULT_TIMEOUT_MILLIS));
    }

    @Override
    public boolean releaseSub(final long timeout, final TimeUnit timeUnit) {
        if (timeout > 0) {
            closeProducer(timeout, timeUnit);
        } else {
            closeProducer(timeoutMillis, TimeUnit.MILLISECONDS);
        }
        return true;
    }

    private void closeProducer(final long timeout, final TimeUnit timeUnit) {
        if (cache.size() > 0) {
            // This thread is a workaround for this Kafka issue: https://issues.apache.org/jira/browse/KAFKA-1660
            final Runnable task = new Runnable() {
                @Override
                public void run() {
                    for (LogProducer logProducer : cache.values()) {
                        logProducer.producer.close();
                    }
                }
            };
            try {
                getLoggerContext().submitDaemon(task).get(timeout, timeUnit);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                // ignore
            }
        }
    }

    public void send(String logId, final byte[] msg) throws ExecutionException, InterruptedException, TimeoutException {
        LogProducer logProducer = getProducer(logId);
        if (logProducer != null) {
            logProducer.send(msg);
        }
    }

    private LogProducer getProducer(String logId) {
        if (cache.containsKey(logId)) {
            return cache.get(logId);
        }
        synchronized (cache) {
            if (!cache.containsKey(logId)) {
                Pair<String, String> kafkaInfo = LogInfoService.getKafkaInfo(logId);
                if (kafkaInfo == null) {
                    return null;
                }
                cache.put(logId, new LogProducer(logId, kafkaInfo.getLeft(), kafkaInfo.getRight()));
            }
        }
        return cache.get(logId);
    }

    public void startup() {
        //producer = producerFactory.newKafkaProducer(config);
        //调用接口获取logId 对应的topic，broker server等信息
        logger.info("Log collector classloader = " + this.getClass().getClassLoader());
    }

    private class LogProducer {
        private String logId;
        private String topic;
        private Producer<byte[], byte[]> producer;
        private final Properties config = new Properties();

        public LogProducer(String logId, String topic, String servers) {
            this.logId = logId;
            this.topic = topic;
            for (Object key : globalConfig.keySet()) {
                config.setProperty((String) key, globalConfig.getProperty((String) key));
            }
            config.setProperty("bootstrap.servers", servers);
            this.producer = producerFactory.newKafkaProducer(this.config);
        }

        public void send(byte[] msg) {
            if (producer != null) {
                producer.send(new ProducerRecord<byte[], byte[]>(topic, msg));
            }
        }
    }
}
