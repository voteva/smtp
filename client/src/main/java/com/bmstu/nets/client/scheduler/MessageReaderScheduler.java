package com.bmstu.nets.client.scheduler;

import com.bmstu.nets.client.queue.MessageQueue;
import com.bmstu.nets.client.service.MessageReaderService;
import com.bmstu.nets.client.service.MessageReaderServiceImpl;
import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.common.model.Message;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static java.lang.Thread.sleep;

public class MessageReaderScheduler
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageReaderScheduler.class);

    private static final long DELAY_MILLIS = 1000L;
    private volatile boolean stopped = false;

    private final MessageQueue messageQueue;
    private final MessageReaderService messageReaderService;

    public MessageReaderScheduler() {
        this.messageQueue = MessageQueue.instance();
        this.messageReaderService = new MessageReaderServiceImpl();
    }

    @Override
    public void run() {
        logger.info("MessageReaderScheduler thread started");
        try {
            while (!stopped) {
                final Message message = messageReaderService.readNextMessage();

                if (message != null) {
                    messageQueue.enqueue(message);
                    logger.debug("Success to add message to queue");
                }

                sleep(DELAY_MILLIS);
            }
        } catch (InterruptedException exception) {
            logger.error("MessageReaderScheduler thread is interrupted");
        }
        logger.info("MessageReaderScheduler thread is stopped");
    }

    @Override
    public void close() {
        stopped = true;
    }
}
