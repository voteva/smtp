package com.bmstu.nets.client;

import lombok.Getter;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ChannelsContext {

    private final Selector selector;
    private final Map<String, Boolean> readyChannelsMap;

    private static final class ChannelsContextHolder {
        static final ChannelsContext INSTANCE = new ChannelsContext();
    }

    @SneakyThrows(IOException.class)
    private ChannelsContext() {
        this.selector = Selector.open();
        this.readyChannelsMap = new ConcurrentHashMap<>();
    }

    public static ChannelsContext instance() {
        return ChannelsContextHolder.INSTANCE;
    }

    public boolean isChannelReady(@Nonnull String domainName) {
        if (readyChannelsMap.get(domainName) == null) {
            return true;
        }
        return readyChannelsMap.get(domainName);
    }

    public void setChannelReady(@Nonnull String domainName) {
        readyChannelsMap.put(domainName, true);
    }

    public void setChannelNotReady(@Nonnull String domainName) {
        readyChannelsMap.put(domainName, false);
    }
}
