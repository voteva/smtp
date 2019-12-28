package com.bmstu.nets.client;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ChannelsContext {

    private final Selector selector;
    private final Map<String, SocketChannel> socketChannelMap;

    private static final class ChannelsContextHolder {
        static final ChannelsContext INSTANCE = new ChannelsContext();
    }

    @SneakyThrows(IOException.class)
    private ChannelsContext() {
        this.selector = Selector.open();
        this.socketChannelMap = new ConcurrentHashMap<>();
    }

    public static ChannelsContext instance() {
        return ChannelsContextHolder.INSTANCE;
    }
}
