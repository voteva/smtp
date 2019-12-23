package com.bmstu.nets.client.handler;

import com.bmstu.nets.client.queue.MessageQueue;
import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.common.model.Message;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static java.lang.Thread.sleep;

public class MessageReader
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageReader.class);

    private static final long DELAY_MILLIS = 1000L;
    private volatile boolean stopped = false;

    private final MessageQueue messageQueue;

    public MessageReader() {
        this.messageQueue = MessageQueue.instance();
    }

    @Override
    public void run() {
        logger.info("MessageReader thread started");
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
            logger.error("MessageReader thread is interrupted");
        }
        logger.info("MessageReader thread is stopped");
    }

    @Override
    public void close() {
        stopped = true;
    }
}
