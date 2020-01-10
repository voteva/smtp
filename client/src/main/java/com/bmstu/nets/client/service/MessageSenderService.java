package com.bmstu.nets.client.service;

import com.bmstu.nets.client.ChannelsContext;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.common.logger.Logger;

import javax.annotation.Nonnull;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static com.bmstu.nets.client.statemachine.Event.CONNECT;
import static com.bmstu.nets.client.statemachine.Mode.READ;
import static com.bmstu.nets.client.statemachine.Mode.WRITE;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MessageSenderService
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageSenderService.class);
    private volatile boolean stopped = false;

    private final Selector selector;

    public MessageSenderService() {
        this.selector = ChannelsContext.instance().getSelector();
    }

    @Override
    public void run() {
        try {
            logger.info("MessageSenderService thread started");

            while (!stopped) {
                selector.selectNow();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isConnectable()) {
                        connect(key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }

                    iterator.remove();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            logger.info("MessageSenderService thread is stopped");
        }
    }

    @Override
    public void close() {
        stopped = true;
    }

    private void connect(@Nonnull SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            socketChannel.finishConnect();

            if (socketChannel.isConnected()) {
                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                Thread.sleep(1000);

                StateMachineContext context = (StateMachineContext) key.attachment();
                context.raise(CONNECT, READ);
            }

        } catch (Exception e) {
            logger.error("Failed to perform connection. Reason: {}", e.getMessage());
            key.cancel();
        }
    }

    private void write(@Nonnull SelectionKey key) {
        StateMachineContext context = (StateMachineContext) key.attachment();
        context.raise(context.getContextHolder().getNextEvent(), WRITE);
    }

    private void read(@Nonnull SelectionKey key) {
        StateMachineContext context = (StateMachineContext) key.attachment();
        context.raise(context.getContextHolder().getNextEvent(), READ);
    }
}
