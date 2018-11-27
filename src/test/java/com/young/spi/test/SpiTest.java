package com.young.spi.test;

import com.young.spi.LogService;
import com.young.spi.impl.Logger;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.ServiceLoader;

public class SpiTest {
    public static void main(String[] args) {
        ServiceLoader<LogService> logServices = ServiceLoader.load(LogService.class);
        for (LogService logService : logServices) {
            logService.info("aaa");
            System.out.println(logService.getClass().getClassLoader());
        }
        LogService logger = new Logger();
        logger.info("bbb");
        System.out.println(logger.getClass().getClassLoader());

        System.out.println(Thread.currentThread().getContextClassLoader());

        System.out.println(SpiTest.class.getClassLoader());
    }
}
