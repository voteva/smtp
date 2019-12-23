package com.bmstu.nets.client.handler;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.common.logger.LoggerFactory;
import com.bmstu.nets.common.model.Message;
import com.bmstu.nets.client.queue.MessageQueue;

import static java.lang.Thread.sleep;

public class MessageReader
        implements Runnable, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(MessageReader.class);

    private static final long DELAY_MILLIS = 1000L;
    private volatile boolean stopped = false;

    private final MessageQueue messageQueue;

    public MessageReader() {
        this.messageQueue = MessageQueue.instance();
    }

    @Override
    public void run() {
        System.out.println("MessageReader thread started");
        try {
            while (!stopped) {
                // TODO read message and add to queue
                Message message = new Message();
                messageQueue.enqueue(message);

                logger.info("Success to add message to queue");
                logger.error("Success to add message to queue");

                sleep(DELAY_MILLIS);
            }
        } catch (InterruptedException exception) {
            System.out.println("MessageReader thread is interrupted");
        }
        System.out.println("MessageReader thread is stopped");
    }

    @Override
    public void close() {
        stopped = true;
    }
}
