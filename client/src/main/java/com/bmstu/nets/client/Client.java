package com.bmstu.nets.client;

import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class Client {
    private static final Logger logger = getLogger(Client.class);

    public static void main(String[] args) {
        logger.info("Client started");
        try {
            new StartupService().start();

            Thread.currentThread().join();
        } catch (InterruptedException e) {
            logger.error("Client stopped");
        }
    }
}
