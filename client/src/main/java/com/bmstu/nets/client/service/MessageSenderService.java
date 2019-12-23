package com.bmstu.nets.client.service;

import com.bmstu.nets.common.model.Message;

import javax.annotation.Nonnull;

public interface MessageSenderService {

    void sendMessage(@Nonnull Message message);
}
