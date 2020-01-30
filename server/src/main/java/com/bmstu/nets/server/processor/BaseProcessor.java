package com.bmstu.nets.server.processor;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.server.model.ServerMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class BaseProcessor {
    private static final Logger logger = getLogger(BaseProcessor.class);

    static HashMap<SocketChannel, Boolean> mailDataMode = new HashMap<>();
    static HashMap<SocketChannel, ByteBuffer> map = new HashMap<>();

    public static boolean process(@Nonnull SocketChannel sc, @Nullable ByteBuffer data, @Nonnull List<ServerMessage> msgs)
            throws IOException {

        if (mailDataMode.get(sc)) {
            // data mode
            logger.info("now in data mode");
            MailDataProcessor.process(sc, data, msgs);

            return true;
        } else {
            // command mode
            logger.info("now in command mode");

            return CommandProcessor.process(sc, data, msgs);
        }
    }

    static void resp(SocketChannel sc, String response) throws IOException {
        response = response.endsWith("\r\n") ? response : response + "\r\n";
        logger.info("Response : " + response);
        byte[] bytes = response.getBytes();
        sc.write(ByteBuffer.wrap(bytes));
    }

    static void setMap(HashMap<SocketChannel, ByteBuffer> map) {
        BaseProcessor.map = map;
    }

    static void setMailDataMode(HashMap<SocketChannel, Boolean> mailDataMode) {
        BaseProcessor.mailDataMode = mailDataMode;
    }
}
