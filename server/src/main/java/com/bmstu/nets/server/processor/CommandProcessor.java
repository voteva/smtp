package com.bmstu.nets.server.processor;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.server.model.MessageStatus;
import com.bmstu.nets.server.model.ServerMessage;
import com.bmstu.nets.server.helper.MessageParser;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class CommandProcessor
        extends BaseProcessor {
    private static final Logger logger = getLogger(CommandProcessor.class);

    public static boolean process(@Nonnull SocketChannel sc, @Nullable ByteBuffer data, @Nonnull List<ServerMessage> msgs)
            throws IOException {

        ByteBuffer prev = map.get(sc);
        if (data != null) {
            prev.put((ByteBuffer) data.flip());
        }

        String txt = new String(prev.array(), 0, prev.position());
        if (!txt.contains("\r\n")) {
            // client not yet finished command
            return true;
        }

        String cmd = new String(map.get(sc).array(), 0, map.get(sc).position());
        logger.info("command : " + cmd);

        if (cmd.startsWith("EHLO ")) {
            resp(sc, "250- SMTP at your service, [127.0.0.1]\r\n250-8BITMIME\r\n250-ENHANCEDSTATUSCODES\r\n250 STARTTLS\r\n");

        } else if (cmd.startsWith("HELO ")) {
            resp(sc, "250 localhost Hello SMTP\r\n");

        } else if (cmd.startsWith("MAIL FROM:")) {
            msgs.add((ServerMessage) new ServerMessage().setSender(MessageParser.parseSender(cmd)));
            resp(sc, "250 2.1.0 Ok\r\n");

        } else if (cmd.startsWith("RCPT TO:")) {
            if (msgs.isEmpty() || isEmpty(msgs.get(msgs.size() - 1).getSender())) {
                resp(sc, "503 5.5.1 Error: need MAIL command\r\n");
                prev.clear();
                return true;
            }

            msgs.get(msgs.size() - 1).addRecipient(MessageParser.parseRecipient(cmd));
            resp(sc, "250 2.1.0 Ok\r\n");

        } else if (cmd.startsWith("RSET")) {
            for (ServerMessage msg : msgs) {
                if (msg.getStatus() == MessageStatus.TMP) {
                    msgs.remove(msg);
                }
            }
            resp(sc, "250 2.0.0 Ok\r\n");

        } else if (cmd.startsWith("VRFY")) {
            resp(sc, "550 5.1.1 All recipients address rejected: User unknown in local recipient table\r\n");

        } else if (cmd.startsWith("DATA")) {
            if (msgs.isEmpty() || msgs.get(msgs.size() - 1).getRecipients().isEmpty()) {
                resp(sc, "503 5.5.1 Error: need RCPT command\r\n");
                prev.clear();
                return true;
            }

            resp(sc, "354 Terminate with line containing only '.' \r\n");
            mailDataMode.put(sc, Boolean.TRUE);

        } else if (cmd.startsWith("QUIT")) {
            resp(sc, "221 Bye !\r\n");
            map.remove(sc);
            mailDataMode.remove(sc);
            prev.clear();
            return false;

        } else {
            resp(sc, "502 5.5.2 Error: command not recognized\r\n");
            logger.info("Warning!!! Unknown command : " + cmd);
        }

        prev.clear();
        return true;
    }
}
