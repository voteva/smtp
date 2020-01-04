package com.bmstu.nets.server;

import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class ServerApplication {
    private static final Logger LOG = getLogger(ServerApplication.class);

    public static void main(String[] args) {
        LOG.info("ServerApplication started");
        int bindPort = 2525;
        if(args.length == 1)
        {
            LOG.info("Setting port to " + args[0]);
            bindPort = Integer.parseInt(args[0]);
        }
        else
        {
            LOG.info("Default to 2525 (︶^︶)");
        }

        try {
            new StartupService().start(bindPort);

            Thread.currentThread().join();
        } catch (InterruptedException e) {
            LOG.error("ServerApplication stopped");
        }
    }
}
