package com.young.spi.impl;

import com.young.spi.LogService;

public class Logger implements LogService {
    @Override
    public void info(String info) {
        System.out.println(info);
    }
}
