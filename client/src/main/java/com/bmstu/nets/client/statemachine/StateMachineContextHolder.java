package com.bmstu.nets.client.statemachine;

import com.bmstu.nets.client.model.Message;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

@Data
@Accessors(chain = true)
public class StateMachineContextHolder {
    private String mxRecord;
    private Message message;
    private Event nextEvent;
    private Selector selector;
    private SelectionKey selectionKey;

    private Socket socket;
    private BufferedReader socketReader;
    private BufferedWriter socketWriter;
}
