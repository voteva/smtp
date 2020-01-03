package com.bmstu.nets.client.service;

import com.bmstu.nets.client.model.Message;

import javax.annotation.Nonnull;
import java.util.List;

public interface MessageReaderService {
    @Nonnull
    List<Message> readNewMessages();
}
