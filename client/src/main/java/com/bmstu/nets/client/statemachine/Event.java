package com.bmstu.nets.client.statemachine;

public enum Event {
    CONNECT,
    HELO,
    MAIL_FROM,
    RCPT_TO,
    DATA_REQUEST,
    DATA,
    QUIT,
    ERROR
}
