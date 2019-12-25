package com.bmstu.nets.client.service;

import com.bmstu.nets.client.model.Message;

import javax.annotation.Nonnull;

public interface MessageSenderService {

    void sendMessage(@Nonnull Message message);
}
