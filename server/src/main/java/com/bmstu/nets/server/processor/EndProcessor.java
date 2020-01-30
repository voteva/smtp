package com.bmstu.nets.server.processor;

import com.bmstu.nets.common.logger.Logger;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class EndProcessor
        extends BaseProcessor {
    private static final Logger logger = getLogger(EndProcessor.class);

    public static void process(SocketChannel sc) throws IOException {
        map.remove(sc);
        resp(sc, "550 The server failed\r\n");
        mailDataMode.remove(sc);
        sc.close();
        logger.info("Warning!!! Unexpected close of socket");
    }
}
