package com.bmstu.nets.client;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.channels.Selector;

public class ChannelsContext {

    @Getter
    private final Selector selector;

    // TODO add Map<MX, SocketChannel>

    private static final class ChannelsContextHolder {
        static final ChannelsContext INSTANCE = new ChannelsContext();
    }

    @SneakyThrows(IOException.class)
    private ChannelsContext() {
        this.selector = Selector.open();
    }

    public static ChannelsContext instance() {
        return ChannelsContextHolder.INSTANCE;
    }
}
