package com.bmstu.nets.client.statemachine;

import com.bmstu.nets.common.model.MessageStatus;
import com.google.common.collect.Table;

public class StateMachine {

    private Table<MessageStatus, Event, Action> table;

    public StateMachine setTable(Table<MessageStatus, Event, Action> table) {
        this.table = table;
        return this;
    }
}
