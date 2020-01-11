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
import java.util.concurrent.Executors;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

/**
 *
 * @author patutinaam
 */
public class MessageReceiver implements Runnable, AutoCloseable{

    private final int port;
    private static final Logger LOG = getLogger(MessageReceiver.class);
    private ServerSocketChannel ssc;
    private boolean stop = false;
    
    private HashMap<SocketChannel, Boolean>    mode = new HashMap<>();
    private HashMap<SocketChannel, ByteBuffer> map  = new HashMap<>();
    private HashMap<SelectionKey, ArrayList<ServerMessage>> messagesHash  = new HashMap<>();


    MessageReceiver(int port)
    {
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
            LOG.info("Starting SMTP  (▀̿̿Ĺ̯̿▀̿ ̿)");
            InetSocketAddress bindAddress = new InetSocketAddress(port);
            ssc = ServerSocketChannel.open();

            ssc.configureBlocking(false);
            ssc.socket().bind(bindAddress);

            final Selector acpt_sel = Selector.open();
            ssc.register(acpt_sel, SelectionKey.OP_ACCEPT);
            
            LOG.info("I am OK! Listen you (ーー;)");
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    while (!stop) {
//                        LOG.info("Connect me ('・ω・')");
                        if (acpt_sel.select() <= 0) {
                            LOG.info("Nothing happend (-_-;)");
                            continue;
                        }
                        Iterator<SelectionKey> iter = acpt_sel.selectedKeys().iterator();
                        while (iter.hasNext()) {
                            SelectionKey sk = iter.next();
                            iter.remove();
                            if (!sk.isValid()) {
                                LOG.error("Error!!! Invalid key!!! (T_T)");
                                error(sk);
                                return;
                            } else if (sk.isAcceptable()) {
                                accept(sk);
                            } else if (sk.isReadable()) {
                                read(sk);
                            } else if (sk.isWritable()) {
                                LOG.info("Warning!!! Write is not registry !!! (T_T)");
                            } else {
                                LOG.info("Warning!!! Unknown status !!! (T_T)");
                            }
                        }
                    }

                    LOG.info("MessageReceiver stoped (｡ŏ﹏ŏ)");
                    ssc.socket().close();
                } catch (Exception e) {
                    LOG.error("Error!!! I catch exception!!! (T_T)\n" + e.toString());
                    notifyAllAboutError(acpt_sel);
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace(System.err);        
        }
    }
    
     private void accept(SelectionKey sk) throws Exception {
        ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(sk.selector(), SelectionKey.OP_READ);
        ConnectProcessor.process(sc);
    }

    private void read(SelectionKey sk) throws IOException {
        SocketChannel sc = (SocketChannel) sk.channel();
        if (!messagesHash.containsKey(sk)) {
            messagesHash.put(sk, new ArrayList<>());
        }
        ArrayList<ServerMessage> msgs = messagesHash.get(sk);
        ByteBuffer buffer = ByteBuffer.allocate(1024); // TODO: more 1024
        boolean is_ok = true;
        while (is_ok && sc.read(buffer) > 0) {
            if(!BaseProcessor.process(sc, buffer, msgs)) {
                is_ok = false;
                sc.close();
            }
            buffer.clear();
        }
        if (is_ok) sk.interestOps(SelectionKey.OP_READ);
    }

    private void error(SelectionKey sk) throws Exception {
        SocketChannel sc = (SocketChannel) sk.channel();
        EndProcessor.process(sc);
    }


    private void notifyAllAboutError(Selector acpt_sel) {
        for (SelectionKey sk : acpt_sel.keys()) {
            SocketChannel sc = (SocketChannel) sk.channel();
            try {
                EndProcessor.process(sc);
//                sc.close();
            } catch (IOException ex) {
                LOG.error("Error!!! I catch exception while SocketChannel notify and close!!! (T_T)\n" + ex.toString());
                ex.printStackTrace();
            }
        }
        try {
            ssc.socket().close();
        } catch (IOException ex) {
            LOG.error("Error!!! I catch exception while ServerSocketChannel close!!! (T_T)\n" + ex.toString());
            ex.printStackTrace();
        }
    }
}
