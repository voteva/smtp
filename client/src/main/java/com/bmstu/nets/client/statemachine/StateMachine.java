package com.bmstu.nets.client.statemachine;

import com.bmstu.nets.client.model.Message;
import com.google.common.collect.Table;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class StateMachine {

    private Table<Event, EventStatus, Action> table;

    public StateMachine setTable(Table<Event, EventStatus, Action> table) {
        this.table = table;
        return this;
    }

    public void raise(Event event, EventStatus status, StateMachineContext context) {
        new StateMachineContextImpl(context).raise(event, status);
    }

    private StateMachineContextImpl createContext(StateMachineContext context) {
        return new StateMachineContextImpl(context);
    }

    private class StateMachineContextImpl
            implements StateMachineContext {

        private final StateMachineContext context;

        private StateMachineContextImpl(StateMachineContext context) {
            this.context = context;
        }

        @Override
        public void raise(Event event, EventStatus status) {
//            scheduler.schedule(() -> {
            final Action action = table.get(event, status);
            action.execute(createContext(context));
//            }, DateUtils.addSeconds(new Date(), 1));
        }

        @Override
        public String getMxRecord() {
            return context.getMxRecord();
        }

        @Override
        public Message getMessage() {
            return context.getMessage();
        }

        @Override
        public BufferedReader getSocketReader() {
            return context.getSocketReader();
        }

        @Override
        public BufferedWriter getSocketWriter() {
            return context.getSocketWriter();
        }
    }
}
