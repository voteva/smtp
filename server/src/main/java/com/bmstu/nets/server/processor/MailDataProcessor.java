package com.bmstu.nets.server.processor;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.server.model.ServerMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MailDataProcessor
        extends BaseProcessor {
    private static final Logger logger = getLogger(MailDataProcessor.class);

    public static boolean process(@Nonnull SocketChannel sc, @Nullable ByteBuffer data, @Nonnull List<ServerMessage> msgs)
            throws IOException {

        ByteBuffer prev = map.get(sc);
        if (data != null) {
            prev.put((ByteBuffer) data.flip());
        }

        String mail = new String(prev.array(), 0, prev.position());

        boolean end = mail.endsWith("\r\n.\r\n");
        logger.info("Message : " + mail + ", endsWith'.' : " + end);
        msgs.get(msgs.size() - 1).setData(mail);

        if (end) {
            msgs.get(msgs.size() - 1).moveToNew();
            mailDataMode.put(sc, Boolean.FALSE); // back to command mode
            prev.clear();
            resp(sc, "250 2.0.0 Ok: got it {messageId}");
        }

        return true;
    }
}
