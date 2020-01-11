package com.bmstu.nets.server.processor;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.bmstu.nets.server.processor.BaseProcessor.setMap;
import static com.bmstu.nets.server.processor.CommandProcessor.process;
import static org.junit.Assert.*;

public class CommandProcessorTest {

    private static HashMap<SocketChannel, ByteBuffer> map  = new HashMap<>();

    @Test
    public void testHelo() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("HELO localhost".getBytes());
        bb.position(14);
        map.put(sc, bb);
        setMap(map);

        assertTrue(process(sc, new ArrayList<>()));
    }

    @Test
    public void testEhlo() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("EHLO localhost".getBytes());
        bb.position(14);
        map.put(sc, bb);
        setMap(map);

        assertTrue(process(sc, new ArrayList<>()));
    }

    @Test
    public void testMail() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("MAIL FROM: <localhost@kkk>".getBytes());
        bb.position(26);
        map.put(sc, bb);
        setMap(map);

        assertTrue(process(sc, new ArrayList<>()));
    }

    @Test
    public void testRcpt() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("RCPT TO: <localhost@kkk>".getBytes());
        bb.position(24);
        map.put(sc, bb);
        setMap(map);

        assertTrue(process(sc, new ArrayList<>()));
    }

    @Test
    public void testRset() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("RSET".getBytes());
        bb.position(4);
        map.put(sc, bb);

        assertTrue(process(sc, new ArrayList<>()));
    }

    @Test
    public void testVerify() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("VRFY sjs".getBytes());
        bb.position(8);
        map.put(sc, bb);
        setMap(map);

        assertTrue(process(sc, new ArrayList<>()));
    }

    @Test
    public void testQuit() throws IOException {
        ServerSocketChannel connector = ServerSocketChannel.open();
        connector.socket().bind(null);
        SocketChannel sc = SocketChannel.open(connector.socket().getLocalSocketAddress());

        ByteBuffer bb = ByteBuffer.wrap("QUIT".getBytes());
        bb.position(4);
        map.put(sc, bb);
        setMap(map);

        assertFalse(process(sc, new ArrayList<>()));
    }
}