/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmstu.nets.server;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.server.model.ServerMessage;
import com.bmstu.nets.server.processor.BaseProcessor;
import com.bmstu.nets.server.processor.ConnectProcessor;
import com.bmstu.nets.server.processor.EndProcessor;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MessageReceiver
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageReceiver.class);

    private final HashMap<SelectionKey, ArrayList<ServerMessage>> messagesHash = new HashMap<>();

    private final int port;
    private ServerSocketChannel ssc;
    private boolean stop = false;

    MessageReceiver(int port) {
        this.port = port;
    }

    @Override
    public void close() throws Exception {
        ssc.socket().close();
        stop = true;
    }

    @Override
    public void run() {
        try {
            logger.info("Starting SMTP  (▀̿̿Ĺ̯̿▀̿ ̿)");
            InetSocketAddress bindAddress = new InetSocketAddress(port);
            ssc = ServerSocketChannel.open();

            ssc.configureBlocking(false);
            ssc.socket().bind(bindAddress);

            final Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("I am OK! Listen you (ーー;)");
            try {
                while (!stop) {
                    if (selector.select() <= 0) {
                        logger.info("Nothing happened (-_-;)");
                        continue;
                    }
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey sk = iter.next();
                        iter.remove();
                        if (!sk.isValid()) {
                            logger.error("Error!!! Invalid key!!! (T_T)");
                            error(sk);
                            return;
                        } else if (sk.isAcceptable()) {
                            accept(sk);
                        } else if (sk.isReadable()) {
                            read(sk);
                        } else if (sk.isWritable()) {
                            logger.info("Warning!!! Write is not registry !!! (T_T)");
                        } else {
                            logger.info("Warning!!! Unknown status !!! (T_T)");
                        }
                    }
                }

                logger.info("MessageReceiver stopped (｡ŏ﹏ŏ)");
                ssc.socket().close();
            } catch (Exception e) {
                logger.error("Error!!! I catch exception!!! (T_T)\n" + e.toString());
                notifyAllAboutError(selector);
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    private void accept(@Nonnull SelectionKey sk) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(sk.selector(), SelectionKey.OP_READ);
        ConnectProcessor.process(sc);
    }

    private void read(@Nonnull SelectionKey sk) throws IOException {
        SocketChannel sc = (SocketChannel) sk.channel();
        if (!messagesHash.containsKey(sk)) {
            messagesHash.put(sk, new ArrayList<>());
        }

        ArrayList<ServerMessage> msgs = messagesHash.get(sk);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        boolean is_ok = true;
        while (is_ok && sc.read(buffer) > 0) {
            if (!BaseProcessor.process(sc, buffer, msgs)) {
                is_ok = false;
                sc.close();
            }
            buffer.clear();
        }
        if (is_ok) sk.interestOps(SelectionKey.OP_READ);
    }

    private void error(SelectionKey sk) throws IOException {
        SocketChannel sc = (SocketChannel) sk.channel();
        EndProcessor.process(sc);
    }

    private void notifyAllAboutError(@Nonnull Selector selector) {
        for (SelectionKey sk : selector.keys()) {
            SocketChannel sc = (SocketChannel) sk.channel();
            try {
                EndProcessor.process(sc);
            } catch (Exception ex) {
                logger.error("Error!!! I catch exception while SocketChannel notify and close!!! (T_T)\n" + ex.toString());
            }
        }
        try {
            ssc.socket().close();
        } catch (IOException ex) {
            logger.error("Error!!! I catch exception while ServerSocketChannel close!!! (T_T)\n" + ex.toString());
        }
    }
}
