package com.bmstu.nets.client.statemachine;

import com.google.common.collect.Table;

public class StateMachine {

    private Table<Event, EventStatus, Action> table;

    public StateMachine setTable(Table<Event, EventStatus, Action> table) {
        this.table = table;
        return this;
    }

    public void raise(Event event, EventStatus status, ContextHolder contextHolder) {
        new StateMachineContextImpl(contextHolder).raise(event, status);
    }

    private StateMachineContextImpl createContext(ContextHolder contextHolder) {
        return new StateMachineContextImpl(contextHolder);
    }

    private class StateMachineContextImpl
            implements StateMachineContext {

        private final ContextHolder contextHolder;

        private StateMachineContextImpl(ContextHolder contextHolder) {
            this.contextHolder = contextHolder;
        }

        @Override
        public void raise(Event event, EventStatus status) {
//            scheduler.schedule(() -> {
            final Action action = table.get(event, status);
            action.execute(createContext(contextHolder));
//            }, DateUtils.addSeconds(new Date(), 1));
        }

        @Override
        public ContextHolder getContextHolder() {
            return contextHolder;
        }
    }
}
