package com.bmstu.nets.server.processor;

import com.bmstu.nets.server.model.ServerMessage;
import com.bmstu.nets.server.msg.Parser;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

import static com.bmstu.nets.server.processor.BaseProcessor.setMap;
import static com.bmstu.nets.server.processor.MailDataProcessor.process;
import static org.junit.Assert.*;

public class MailDataProcessorTest {

    private static HashMap<SocketChannel, ByteBuffer> map  = new HashMap<>();

    @Test
    public void testData() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("I am alive!".getBytes());
        bb.position(11);
        map.put(sc, bb);
        setMap(map);

        ArrayList<ServerMessage> msgs = new ArrayList<>();
        msgs.add((ServerMessage) new ServerMessage().setSender("sjjsjs@sjd"));

        assertTrue(process(sc, null, msgs));
    }

    @Test
    public void testPoint() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("\r\n.\r\n".getBytes());
        bb.position(5);
        map.put(sc, bb);
        setMap(map);

        ArrayList<ServerMessage> msgs = new ArrayList<>();
        msgs.add((ServerMessage) new ServerMessage().setSender("sjjsjs@sjd"));

        assertTrue(process(sc, null, msgs));
    }
}