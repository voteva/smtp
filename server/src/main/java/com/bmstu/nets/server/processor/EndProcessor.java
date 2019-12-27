package com.bmstu.nets.server.processor;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class EndProcessor extends BaseProcessor {
    public static void process(SocketChannel sc) throws IOException {
        map.remove(sc);
        mailDataMode.remove(sc);
        sc.close();
        LOG.info("Warning!!! Unexpected close of socket");
    }
}
