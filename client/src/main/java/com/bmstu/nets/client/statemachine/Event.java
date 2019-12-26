package com.bmstu.nets.client.statemachine;

import lombok.Getter;

public enum Event {
    INIT(""),
    HELO("HELO %s"),
    MAIL_FROM("MAIL FROM: %s"),
    RCPT_TO("RCPT TO: $s"),
    DATA("DATA"),
    QUIT("QUIT");

    @Getter
    private String command;

    Event(String command) {
        this.command = command;
    }
}
