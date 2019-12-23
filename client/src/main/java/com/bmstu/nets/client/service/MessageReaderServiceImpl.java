package com.bmstu.nets.client.service;

import com.bmstu.nets.common.model.Message;

import javax.annotation.Nullable;

import static java.util.Collections.singletonList;

public class MessageReaderServiceImpl
        implements MessageReaderService {

    @Nullable
    @Override
    public Message readNextMessage() {
        // TODO read message from filesystem
        return new Message()
                .setSender("t.voteva@innopolis.ru")
                .setRecipients(singletonList("tatianavoteva@gmailghhv.com"))
                .setSubject("Hello")
                .setData("Message data".getBytes());
    }
}
