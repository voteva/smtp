package com.bmstu.nets.client.statemachine;

import com.bmstu.nets.client.model.Message;
import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Queue;

@Data
@Accessors(chain = true)
public class StateMachineContextHolder {
    private String domain;
    private String mxRecord;
    private Queue<Message> messages;
    private Event nextEvent;
    private Selector selector;
    private SelectionKey selectionKey;
}
