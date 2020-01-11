package com.bmstu.nets.client;

import com.bmstu.nets.client.scheduler.MessageFileReaderScheduler;
import com.bmstu.nets.client.scheduler.MessageQueueReaderScheduler;
import com.bmstu.nets.client.service.MessageSenderService;
import com.bmstu.nets.common.logger.LogWriter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class StartupService {
    private final ExecutorService executorService;

    public StartupService() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Service-%d")
                .setDaemon(true)
                .build();
        this.executorService = Executors.newFixedThreadPool(4, threadFactory);
    }

    public void start() {
        executorService.execute(new LogWriter());
        executorService.execute(new MessageSenderService());
        executorService.execute(new MessageQueueReaderScheduler());
        executorService.execute(new MessageFileReaderScheduler());
    }

    public void stop() {
        executorService.shutdownNow();
    }
}
