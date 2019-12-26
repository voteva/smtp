package com.bmstu.nets.client.statemachine;

import com.bmstu.nets.client.model.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface StateMachineContext {
    void raise(Event event, EventStatus status);
    String getMxRecord();
    Message getMessage();
    BufferedReader getSocketReader();
    BufferedWriter getSocketWriter();
}
