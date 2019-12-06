package com.bmstu.nets.client.queue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogQueue
        implements QueueExchange<String> {
    private final ConcurrentLinkedQueue<String> queue;

    private static final class LogQueueHolder {
        static final LogQueue INSTANCE = new LogQueue();
    }

    private LogQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public static LogQueue instance() {
        return LogQueueHolder.INSTANCE;
    }

    public void enqueue(@Nonnull String item) {
        queue.add(item);
    }

    @Nullable
    public String dequeue() {
        return queue.poll();
    }
}
