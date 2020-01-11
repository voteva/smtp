package com.bmstu.nets.server.processor;

import com.bmstu.nets.server.model.ServerMessage;
import com.bmstu.nets.server.msg.MessageSaver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class MailDataProcessor extends BaseProcessor{
    public static boolean process(SocketChannel sc, ByteBuffer data, ArrayList<ServerMessage> msgs) throws IOException {
        ByteBuffer prev = map.get(sc);
        if (data != null) {
            prev.put((ByteBuffer) data.flip());
        }

        String mail = new String(prev.array(), 0, prev.position());

        boolean end = mail.endsWith("\r\n.\r\n");
        LOG.info("Message : " + mail + ", endsWith'.' : " + end);
        msgs.get(msgs.size() - 1).setData(mail);

        if (end) {
            msgs.get(msgs.size() - 1).to_new();
            mailDataMode.put(sc, Boolean.FALSE);// back to command mode
            prev.clear();
            resp(sc, "250 2.0.0 Ok: got it {messageId}");
        } else {
//            msgs.get(msgs.size() - 1).save();
        }
        return true;
    }
}
