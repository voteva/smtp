package com.bmstu.nets.client;

import com.bmstu.nets.client.handler.MessageReader;
import com.bmstu.nets.client.handler.MessageSender;
import com.bmstu.nets.common.logger.LogWriter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class StartupService {
    private final ExecutorService executorService;

    StartupService() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Service-%d")
                .setDaemon(true)
                .build();
        this.executorService = Executors.newFixedThreadPool(3, threadFactory);
    }

    void start() {
        executorService.execute(new LogWriter());
        executorService.execute(new MessageReader());
        executorService.execute(new MessageSender());
    }
}
