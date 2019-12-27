package com.bmstu.nets.server.processor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ConnectProcessor extends BaseProcessor{
    public static void process(SocketChannel sc) throws IOException {
        resp(sc, "220 Welcome to NT's SMTP \r\n");
        map.put(sc, ByteBuffer.allocate(1024));
        mailDataMode.put(sc, Boolean.FALSE);
    }
}