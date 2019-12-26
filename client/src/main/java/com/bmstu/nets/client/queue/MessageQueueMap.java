package com.bmstu.nets.client.queue;

import com.bmstu.nets.client.model.Message;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueueMap {
    private final Map<String, ConcurrentLinkedQueue<Message>> queueMap;

    private static final class MessageQueueHolder {
        static final MessageQueueMap INSTANCE = new MessageQueueMap();
    }

    private MessageQueueMap() {
        this.queueMap = new ConcurrentHashMap<>();
    }

    public static MessageQueueMap instance() {
        return MessageQueueHolder.INSTANCE;
    }

    @Nonnull
    public List<Message> getAllForDomain(@Nonnull String domain) {
        return new ArrayList<>(queueMap.remove(domain));
    }

    public void putForDomain(@Nonnull String domain, @Nonnull Message message) {
        ConcurrentLinkedQueue<Message> messages = queueMap.get(domain);
        if (messages == null) {
            messages = new ConcurrentLinkedQueue<>();
        }
        messages.add(message);
        queueMap.put(domain, messages);
    }

    @Nonnull
    public List<String> getAllDomains() {
        return new ArrayList<>(queueMap.keySet());
    }
}
