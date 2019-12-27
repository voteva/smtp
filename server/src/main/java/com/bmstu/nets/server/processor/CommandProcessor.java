package com.bmstu.nets.server.processor;

import com.bmstu.nets.server.model.ServerMessage;
import com.bmstu.nets.server.msg.Parser;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class CommandProcessor extends BaseProcessor{
    public static boolean process(SocketChannel sc, ArrayList<ServerMessage> msgs) throws IOException {
        String cmd = new String(map.get(sc).array(), 0, map.get(sc).position());
        LOG.info("command : " + cmd);

        if (cmd.startsWith("EHLO ")) {
            resp(sc, "250- SMTP at your service, [127.0.0.1]\r\n250-8BITMIME\r\n250-ENHANCEDSTATUSCODES\r\n250 STARTTLS\r\n");

        } else if (cmd.startsWith("HELO ")) {
            resp(sc, "250 localhost Hello SMTP\r\n");

        } else if (cmd.startsWith("MAIL FROM:")) {
            msgs.add((ServerMessage) new ServerMessage().setSender(Parser.parseSender(cmd)));
            resp(sc, "250 2.1.0 Ok\r\n");

        } else if (cmd.startsWith("RCPT TO:")) {
            msgs.get(msgs.size() - 1).addRecipient(Parser.parseRecipient(cmd));
            resp(sc, "250 2.1.0 Ok\r\n");

        } else if (cmd.startsWith("RSET")) {
            msgs.clear();
            resp(sc, "250 2.0.0 Ok\r\n");

        } else if (cmd.startsWith("VRFY")) {
            resp(sc, "550 5.1.1 All recipients address rejected: User unknown in local recipient table\r\n");

        } else if (cmd.startsWith("DATA")) {
            resp(sc, "354 Terminate with line containing only '.' \r\n");
            mailDataMode.put(sc, Boolean.TRUE);

        } else if (cmd.startsWith("QUIT")) {
            for (ServerMessage msg : msgs) {
                msg.to_new();
            }
            resp(sc, "221 Bye !\r\n");
            map.remove(sc);
            mailDataMode.remove(sc);
            return false;

        } else {
            resp(sc, "502 5.5.2 Error: command not recognized\r\n");
            LOG.info("Warning!!! Unknown command : " + cmd);
        }
        return true;
    }
}
