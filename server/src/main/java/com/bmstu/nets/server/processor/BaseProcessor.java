package com.bmstu.nets.server.processor;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.server.model.ServerMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class BaseProcessor {
    static HashMap<SocketChannel, Boolean> mailDataMode = new HashMap<>();
    static HashMap<SocketChannel, ByteBuffer> map  = new HashMap<>();
    static final Logger LOG = getLogger(BaseProcessor.class);

    public static boolean process(SocketChannel sc, ByteBuffer data, ArrayList<ServerMessage> msgs) throws IOException {
        if (mailDataMode.get(sc)) {
            // data mode
            LOG.info("now in data mode");
            MailDataProcessor.process(sc, data, msgs);

            return true;
        } else {
            // command mode
            LOG.info("now in command mode");

            return CommandProcessor.process(sc, data, msgs);
        }
    }

    static void resp(SocketChannel sc, String response) throws IOException {
        response = response.endsWith("\r\n") ? response : response + "\r\n";
        LOG.info("Response : " + response);
        byte[] bytes = response.getBytes();
        sc.write(ByteBuffer.wrap(bytes));
    }

    public static void setMap(HashMap<SocketChannel, ByteBuffer> map) {
        BaseProcessor.map = map;
    }

    static void setMailDataMode(HashMap<SocketChannel, Boolean> mailDataMode) {
        BaseProcessor.mailDataMode = mailDataMode;
    }
}
