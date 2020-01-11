package com.bmstu.nets.server.processor;

import com.bmstu.nets.server.model.ServerMessage;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

import static com.bmstu.nets.server.processor.BaseProcessor.*;
import static org.junit.Assert.*;

public class BaseProcessorTest {

    private static HashMap<SocketChannel, ByteBuffer> map  = new HashMap<>();
    private static HashMap<SocketChannel, Boolean> mailDataMode = new HashMap<>();

    @Test
    public void data() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("HELO localhost".getBytes());
        bb.position(14);
        map.put(sc, bb);
        setMap(map);

        mailDataMode.put(sc, true);
        setMailDataMode(mailDataMode);


        ArrayList<ServerMessage> msgs = new ArrayList<>();
        msgs.add((ServerMessage) new ServerMessage().setSender("sjjsjs@sjd"));

        assertTrue(process(sc, null, msgs));
    }

    @Test
    public void command() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("HELO localhost".getBytes());
        bb.position(14);
        map.put(sc, bb);
        setMap(map);

        mailDataMode.put(sc, false);
        setMailDataMode(mailDataMode);

        assertTrue(process(sc, null, new ArrayList<>()));
    }
}