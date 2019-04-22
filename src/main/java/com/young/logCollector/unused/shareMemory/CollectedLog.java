package com.young.logCollector.unused.shareMemory;

public class CollectedLog {
    private byte[] log;

    public byte[] getLog() {
        return log;
    }

    public int size() {
        return log.length;
    }

    public CollectedLog(byte[] log) {
        this.log = log;
    }

    public CollectedLog(String log) {
        this.log = log.getBytes();
    }

    public String logInfo() {
        return new String(log);
    }

}
