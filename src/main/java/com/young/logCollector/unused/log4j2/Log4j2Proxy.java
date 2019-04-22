package com.young.logCollector.unused.log4j2;

import com.sohu.log.collector.LogCollector;
import com.sohu.log.util.Constants;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

//-Dlog4j.configurationFile=/Users/young/Desktop/temp/log-collector/lib/log4j2.xml

public class Log4j2Proxy implements LogCollector {
    private static final String LOG4J2_MANAGER = "org.apache.logging.log4j.LogManager";
    private static final String LOG4J2_MARKERMANAGER = "org.apache.logging.log4j.MarkerManager";
    private static final String LOG4J2_MARKER = "Log4jMarker";
    private static final String MARKER = "Marker";

    private static Method infoMethod;
    private static Object logger;
    private static Method markerMethod;
    private static final ClassLoader CLASS_LOADER = Log4j2ClassLoader.getInstance();
    private String logId;

    private Log4j2Proxy() {
    }

    static {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            setClassLoader(CLASS_LOADER);
            File configFile = new File(Constants.LOG4J2_CONFIG_FILE);
            Class<?> logManagerClazz = CLASS_LOADER.loadClass(LOG4J2_MANAGER);
            Method getContext = logManagerClazz.getMethod("getContext", boolean.class);
            Object loggerContext = getContext.invoke(null, false);
            Class<?> loggerContextClass = loggerContext.getClass();
            Method setConfigLocation = loggerContextClass.getMethod("setConfigLocation", URI.class);
            setConfigLocation.invoke(loggerContext, configFile.toURI());

            Method getLogger = logManagerClazz.getMethod("getLogger", String.class);
            //调用LogManager.getLogger("xxx")
            logger = getLogger.invoke(null, Constants.KAFKA_LOG);
            Class<?> loggerClazz = logger.getClass();

            Class<?> markerManagerClazz = CLASS_LOADER.loadClass(LOG4J2_MARKERMANAGER);
            markerMethod = markerManagerClazz.getMethod("getMarker", String.class);

            Class<?>[] innerClasses = markerManagerClazz.getDeclaredClasses();
            Class<?> markerClass = null;
            //获取 org.apache.logging.log4j.Marker的class
            for (Class<?> innerClass : innerClasses) {
                if (LOG4J2_MARKER.equals(innerClass.getSimpleName())){
                    Class<?>[] interfaces = innerClass.getInterfaces();
                    for (Class<?> superInterface : interfaces) {
                        if (MARKER.equals(superInterface.getSimpleName())){
                            markerClass = superInterface;
                        }
                    }
                }
            }
            //info方法
            infoMethod = loggerClazz.getMethod("info", new Class[]{markerClass, String.class});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setClassLoader(oldClassLoader);
        }
    }

    @Override
    public void collect(String message) {
        try {
            doLog(this.logId, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLog(String logId, String message) throws Exception {
        //logger.info(marker, message)
        Object marker = marker(logId);
        infoMethod.invoke(logger, new Object[]{marker, message});
    }

    private Object marker(String logId) throws Exception {
        //相当于MarkerManager.getMarker(logId);
        return markerMethod.invoke(null, logId);
    }

    public static Log4j2Proxy build(String logId) {
        Log4j2Proxy logProxy = new Log4j2Proxy();
        logProxy.logId = logId;
        /*File logPath = new File(Constants.LOGS_BASE_PATH + File.separator + logId);
        if (!logPath.exists()) {
            if (!logPath.mkdirs()) {
                System.out.println("Create directory failed! dir = " + logPath.getAbsolutePath());
            }
        }*/
        return logProxy;
    }

    private static ClassLoader setClassLoader(ClassLoader classLoader) {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        //这里设置线程上下文classloader为我们自定义的loader
        Thread.currentThread().setContextClassLoader(classLoader);
        return oldClassLoader;
    }

    @Override
    public String getLogId() {
        return logId;
    }

    @Override
    public long hasCollected() {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
