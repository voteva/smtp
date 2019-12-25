package com.bmstu.nets.client.service;

import com.bmstu.nets.client.model.Message;

import javax.annotation.Nonnull;
import java.util.List;

public interface MessageSenderService {

    void sendMessages(@Nonnull String domain, @Nonnull List<Message> messages);
}
