package com.bmstu.nets.client.queue;

import com.bmstu.nets.client.model.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue
        implements QueueExchange<Message> {
    private final ConcurrentLinkedQueue<Message> queue;

    private static final class MessageQueueHolder {
        static final MessageQueue INSTANCE = new MessageQueue();
    }

    private MessageQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public static MessageQueue instance() {
        return MessageQueueHolder.INSTANCE;
    }

    public void enqueue(@Nonnull Message message) {
        queue.add(message);
    }

    @Nullable
    public Message dequeue() {
        return queue.poll();
    }
}
