package com.bmstu.nets.client.scheduler;

import com.bmstu.nets.common.logger.Logger;

import java.nio.channels.Selector;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MessageSender
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageSender.class);

    private volatile boolean stopped = false;

    public MessageSender() {

    }

    @Override
    public void run() {
        try {
            logger.info("MessageSender thread started");
            Selector selector = Selector.open();

            while (!stopped) {

            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            logger.info("MessageSender thread is stopped");
        }
    }

    @Override
    public void close() {
        stopped = true;
    }
}
