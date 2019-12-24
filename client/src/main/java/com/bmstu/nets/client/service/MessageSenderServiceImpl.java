package com.bmstu.nets.client.service;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.common.model.Message;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;
import java.util.Map;

import static com.bmstu.nets.client.utils.MailUtils.getMxRecord;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class MessageSenderServiceImpl
        implements MessageSenderService {
    private static final Logger logger = getLogger(MessageSenderServiceImpl.class);

    private Map<String, SocketChannel> channels;
    private ByteBuffer buffer;
    private Selector selector;

    // https://gist.github.com/yukaizhao/155d931326e298d6404f
    private List<> pendingChanges;

    @SneakyThrows
    private MessageSenderServiceImpl() {
        this.channels = newHashMap();
        this.selector = this.initSelector();
        this.buffer = ByteBuffer.allocate(256);

        this.pendingChanges = newArrayList();
    }

    @Override
    public void sendMessage(@Nonnull Message message) {
        message.getRecipients().forEach(recipient -> {
            final String mxRecord = getMxRecord(recipient);
            if (mxRecord != null) {
                send(message, mxRecord);
            } else {
                logger.warn("No mxRecord found for recipient '{}'", recipient);
            }
        });
    }

    private void send(@Nonnull Message message, @Nonnull String mxRecord) {
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(mxRecord, 25));

            SelectionKey key = channel.register(selector, SelectionKey.OP_READ);

        } catch (Exception e) {
            logger.error("Failed to send message");
        }
    }

    @SneakyThrows(IOException.class)
    private SocketChannel initiateConnection() {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        socketChannel.connect(new InetSocketAddress(this.hostAddress, 25));

        // Queue a channel registration since the caller is not the
        // selecting thread. As part of the registration we'll register
        // an interest in connection events. These are raised when a channel
        // is ready to complete connection establishment.
        synchronized (this.pendingChanges) {
            this.pendingChanges.add(new ChangeRequest(socketChannel, ChangeRequest.REGISTER, SelectionKey.OP_CONNECT));
        }

        // Queue a channel registration since the caller is not the selecting thread. As part of the registration we'll register an interest in connection events. These are raised when a channel is ready to complete connection establishment.

        return socketChannel;
    }

    @SneakyThrows(IOException.class)
    private Selector initSelector() {
        return SelectorProvider.provider().openSelector();
    }
}
