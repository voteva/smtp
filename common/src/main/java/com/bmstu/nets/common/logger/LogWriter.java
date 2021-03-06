package com.bmstu.nets.common.logger;

import com.bmstu.nets.common.queue.LogQueue;

public class LogWriter
        implements Runnable, AutoCloseable {
    private volatile boolean stopped = false;

    private final LogQueue logQueue;

    public LogWriter() {
        this.logQueue = LogQueue.instance();
    }

    @Override
    public void run() {
        System.out.println("LogWriter thread started");

        while (!stopped) {
            String logMessage = logQueue.dequeue();
            if (logMessage != null) {
                System.out.println(logMessage);
            }
        }

        System.out.println("LogWriter thread is stopped");
    }

    @Override
    public void close() {
        stopped = true;
    }
}
