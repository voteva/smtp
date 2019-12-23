package com.bmstu.nets.client.handler;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.common.model.Message;
import com.bmstu.nets.client.queue.MessageQueue;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static java.lang.Thread.sleep;

public class MessageSender
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageSender.class);

    private static final long DELAY_MILLIS = 1000L;
    private volatile boolean stopped = false;

    private final MessageQueue messageQueue;

    public MessageSender() {
        this.messageQueue = MessageQueue.instance();
    }

    @Override
    public void run() {
        logger.info("MessageSender thread started");
        try {
            while (!stopped) {
                // TODO read message from queue and execute send
                Message message = messageQueue.dequeue();

                sleep(DELAY_MILLIS);
            }
        } catch (InterruptedException exception) {
            logger.error("MessageSender thread is interrupted");
        }
        logger.info("MessageSender thread is stopped");
    }

    @Override
    public void close() {
        stopped = true;
    }
}
