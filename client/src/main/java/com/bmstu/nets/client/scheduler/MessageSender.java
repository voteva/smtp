package com.bmstu.nets.client.scheduler;

import com.bmstu.nets.client.ChannelsContext;
import com.bmstu.nets.common.logger.Logger;

import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MessageSender
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageSender.class);

    private volatile boolean stopped = false;

    private final Selector selector;

    public MessageSender() {
        this.selector = ChannelsContext.instance().getSelector();
    }

    @Override
    public void run() {
        try {
            logger.info("MessageSender thread started");
            int totalKeys;

            while (!stopped) {
                totalKeys = selector.select();

                if (totalKeys > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                // ignore
                            } else if (key.isConnectable()) {
                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                socketChannel.finishConnect();

                                if (socketChannel.isConnected()) {
                                    key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                                }
                            } else if (key.isReadable()) {

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            logger.info("MessageSender thread is stopped");
        }
    }

    @Override
    public void close() {
        stopped = true;
    }
}
