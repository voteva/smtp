package com.bmstu.nets.client.handler;

import com.bmstu.nets.common.model.Message;
import com.bmstu.nets.client.queue.MessageQueue;

import static java.lang.Thread.sleep;

public class MessageSender
        implements Runnable, AutoCloseable {
    private static final long DELAY_MILLIS = 1000L;
    private volatile boolean stopped = false;

    private final MessageQueue messageQueue;

    public MessageSender() {
        this.messageQueue = MessageQueue.instance();
    }

    @Override
    public void run() {
        System.out.println("MessageSender thread started");
        try {
            while (!stopped) {
                // TODO read message from queue and execute send
                Message message = messageQueue.dequeue();

                sleep(DELAY_MILLIS);
            }
        } catch (InterruptedException exception) {
            System.out.println("MessageSender thread is interrupted");
        }
        System.out.println("MessageSender thread is stopped");
    }

    @Override
    public void close() {
        stopped = true;
    }
}
