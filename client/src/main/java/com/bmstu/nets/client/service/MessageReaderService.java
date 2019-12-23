package com.bmstu.nets.client.service;

import com.bmstu.nets.common.model.Message;

import javax.annotation.Nullable;

public interface MessageReaderService {

    @Nullable
    Message readNextMessage();
}
