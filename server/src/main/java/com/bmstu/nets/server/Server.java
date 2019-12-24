/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmstu.nets.server;

import com.bmstu.nets.common.model.Message;
import com.bmstu.nets.common.model.MessageStatus;
import com.bmstu.nets.server.logger.Logger;
import com.bmstu.nets.server.msg.MessageSaver;
import com.bmstu.nets.server.msg.Parser;

import java.io.IOException;
import java.net.*;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executors;

/**
 *
 * @author patutinaam
 */
public class Server {

    private final int port;
    private static final Logger LOG = new Logger();
    private ServerSocketChannel ssc;
    private boolean stop = false;
    
    private HashMap<SocketChannel, Boolean>    mode = new HashMap<>();
    private HashMap<SocketChannel, ByteBuffer> map  = new HashMap<>();
    private HashMap<SelectionKey, ArrayList<Message>> messagesHash  = new HashMap<>();


    public Server(int port)
    {
            this.port = port;
    }
    
    public void stop() throws Exception{
        ssc.socket().close();
        stop = true;
    }
        
    public void startSMTP() {
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
                        LOG.info("Connect me ('・ω・')");
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
                                emit("error", null, null, null);
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

                    LOG.info("Server stoped (｡ŏ﹏ŏ)");
                    ssc.socket().close();
                } catch (Exception e) {
                    LOG.error("Error!!! I catch exception!!! (T_T)\n" + e.toString());
                    throw new RuntimeException(e);
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
        emit("connect", null, sc, null);
    }

    private void read(SelectionKey sk) throws Exception {
        SocketChannel sc = (SocketChannel) sk.channel();
        if (!messagesHash.containsKey(sk)) {
            messagesHash.put(sk, new ArrayList<>());
        }
        ArrayList<Message> msgs = messagesHash.get(sk);
        ByteBuffer buffer = ByteBuffer.allocate(1024); // TODO: 1024 is ok?
        while (sc.read(buffer) > 0) {
            emit("data", buffer, sc, msgs);
            buffer.clear();
        }
        sk.interestOps(SelectionKey.OP_READ);
    }

    public void emit(String event, ByteBuffer data, SocketChannel sc, ArrayList<Message> msgs) throws Exception {
        if ("data".equals(event)) {

            if (mode.get(sc)) {
                // data mode
                LOG.info("now in data mode");
                emit("mailData", data, sc, msgs);
            } else {
                // command mode
                LOG.info("now in command mode");
                ByteBuffer prev = map.get(sc);
                if (data != null) {
                    prev.put((ByteBuffer) data.flip());
                }

                String txt = new String(prev.array(), 0, prev.position());
                if (!txt.contains("\r\n")) {
                    return;// client not yet finished command
                }
                emit("command", null, sc, msgs);
                prev.clear();// start over for next request.
            }
        } else if ("command".equals(event)) {

            String cmd = new String(map.get(sc).array(), 0, map.get(sc).position());
            LOG.info("command : " + cmd);

            if (cmd.startsWith("EHLO ")) {
                resp(sc, "250-OLCNTI001302 at your service, [127.0.0.1]\r\n250-8BITMIME\r\n250-ENHANCEDSTATUSCODES\r\n250 STARTTLS\r\n");

            } else if (cmd.startsWith("HELO ")) {
                resp(sc, "250 localhost Hello OLCNTI001302\r\n");

            } else if (cmd.startsWith("MAIL ")) {
                msgs.add(new Message().setSender(Parser.parseSender(cmd)));
                resp(sc, "250 2.1.0 Ok\r\n");

            } else if (cmd.startsWith("RCPT ")) {
                msgs.get(msgs.size() - 1).addRecipient(Parser.parseRecipient(cmd));
                resp(sc, "250 2.1.0 Ok\r\n");

            } else if (cmd.startsWith("DATA")) {
                resp(sc, "354 Terminate with line containing only '.' \r\n");
                mode.put(sc, Boolean.TRUE);

            } else if (cmd.startsWith("QUIT")) {
                resp(sc, "221 {HOSTNAME} Bye !\r\n");
                map.remove(sc);
                mode.remove(sc);

            } else {
                LOG.info("Warning!!! Unknown command : " + cmd);
            }
        } else if ("mailData".equals(event)) {

            ByteBuffer prev = map.get(sc);
            if (data != null) {
                prev.put((ByteBuffer) data.flip());
            }

            String mail = new String(prev.array(), 0, prev.position());

            boolean end = mail.endsWith("\r\n.\r\n");
            LOG.info("Message : " + mail + ", endsWith'.' : " + end);

            if (end) {
                msgs.get(msgs.size() - 1).setData(mail.getBytes());
                msgs.get(msgs.size() - 1).setStatus(MessageStatus.NEW);
                MessageSaver.save(msgs.get(msgs.size() - 1));
                mode.put(sc, Boolean.FALSE);// back to command mode
                prev.clear();
                resp(sc, "250 2.0.0 Ok: got it {messageId}");
            }
        } else if ("connect".equals(event)) {

            resp(sc, "220 Welcome to NT's SMTP \r\n");
            map.put(sc, ByteBuffer.allocate(1024));
            mode.put(sc, Boolean.FALSE);
        } else if ("end".equals(event)) {
            map.remove(sc);
            mode.remove(sc);
            LOG.info("Warning!!! Unexpected close of socket");
        } else if ("error".equals(event)) {
            LOG.error("Error!!! (T_T)");
        }
    }
    
    private void resp(SocketChannel sc, String response) throws IOException {
        response = response.endsWith("\r\n") ? response : response + "\r\n";
        LOG.info("Response : " + response);
        byte[] bytes = response.getBytes();
        sc.write(ByteBuffer.wrap(bytes));
    }
        
   public static void main(String[] args) {
           int bindPort = 2525;
           if(args.length == 1)
           {
                   LOG.info("Setting port to " + args[0]);
                   bindPort = Integer.parseInt(args[0]);
           }
           else
           {
                   LOG.info("Default to 2525 (︶^︶)");
           }
           Server console = new Server(bindPort);
           console.startSMTP();
   }
	
    
}
