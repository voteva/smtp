package com.bmstu.nets.client;

import com.bmstu.nets.client.statemachine.Event;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ChannelsContext {

    private final Selector selector;
    private final Map<SelectionKey, Event> selectionKeyEvent;

    // TODO add Map<MX, SocketChannel>

    private static final class ChannelsContextHolder {
        static final ChannelsContext INSTANCE = new ChannelsContext();
    }

    @SneakyThrows(IOException.class)
    private ChannelsContext() {
        this.selector = Selector.open();
        this.selectionKeyEvent = new ConcurrentHashMap<>();
    }

    public static ChannelsContext instance() {
        return ChannelsContextHolder.INSTANCE;
    }
}
