package com.bmstu.nets.server.processor;

import com.bmstu.nets.server.logger.Logger;
import com.bmstu.nets.server.model.ServerMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

public class BaseProcessor {
    protected static HashMap<SocketChannel, Boolean> mailDataMode = new HashMap<>();
    protected static HashMap<SocketChannel, ByteBuffer> map  = new HashMap<>();
    protected static final Logger LOG = new Logger();

    public static boolean process(SocketChannel sc, ByteBuffer data, ArrayList<ServerMessage> msgs) throws IOException {
        if (mailDataMode.get(sc)) {
            // data mode
            LOG.info("now in data mode");
            MailDataProcessor.process(sc, data, msgs);
            return true;
        } else {
            // command mode
            LOG.info("now in command mode");
            ByteBuffer prev = map.get(sc);
            if (data != null) {
                prev.put((ByteBuffer) data.flip());
            }

            String txt = new String(prev.array(), 0, prev.position());
            if (!txt.contains("\r\n")) {
                return true;// client not yet finished command
            }
            boolean is_ok = CommandProcessor.process(sc, msgs);
            prev.clear();// start over for next request.
            return is_ok;
        }
    }

    protected static void resp(SocketChannel sc, String response) throws IOException {
        response = response.endsWith("\r\n") ? response : response + "\r\n";
        LOG.info("Response : " + response);
        byte[] bytes = response.getBytes();
        sc.write(ByteBuffer.wrap(bytes));
    }

}
