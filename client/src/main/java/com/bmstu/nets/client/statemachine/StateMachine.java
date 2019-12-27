package com.bmstu.nets.client.statemachine;

import com.google.common.collect.Table;

public class StateMachine {

    private Table<Event, EventStatus, Action> table;

    public StateMachine setTable(Table<Event, EventStatus, Action> table) {
        this.table = table;
        return this;
    }

    public void raise(Event event, EventStatus status, StateMachineContextHolder contextHolder) {
        new StateMachineContextImpl(contextHolder).raise(event, status);
    }

    private StateMachineContextImpl createContext(StateMachineContextHolder contextHolder) {
        return new StateMachineContextImpl(contextHolder);
    }

    private class StateMachineContextImpl
            implements StateMachineContext {

        private final StateMachineContextHolder contextHolder;

        private StateMachineContextImpl(StateMachineContextHolder contextHolder) {
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
        public StateMachineContextHolder getContextHolder() {
            return contextHolder;
        }
    }
}
