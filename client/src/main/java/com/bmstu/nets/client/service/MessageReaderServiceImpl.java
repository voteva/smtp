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
                .setRecipients(singletonList("tatianavoteva@gmail.com"))
                .setSubject("Hello")
                .setData(("From: t.voteva@innopolis.ru\n" +
                        "To: tatianavoteva@gmail.com\n" +
                        "Subject: Hello\n" +
                        "\n" +
                        "Message data").getBytes());
    }
}
