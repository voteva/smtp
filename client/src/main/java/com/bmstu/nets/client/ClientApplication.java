package com.bmstu.nets.client;

import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class ClientApplication {
    private static final Logger logger = getLogger(ClientApplication.class);

    public static void main(String[] args) {
        logger.info("ClientApplication started");
        try {
            new StartupService().start();

            Thread.currentThread().join();
        } catch (InterruptedException e) {
            logger.error("ClientApplication stopped");
        }
    }
}
