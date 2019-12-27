package com.bmstu.nets.client.statemachine;

import com.google.common.collect.Table;

public class StateMachine {

    private Table<Event, Mode, Action> table;

    public StateMachine setTable(Table<Event, Mode, Action> table) {
        this.table = table;
        return this;
    }

    public void raise(Event event, Mode status, StateMachineContextHolder contextHolder) {
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
        public void raise(Event event, Mode mode) {
            final Action action = table.get(event, mode);
            action.execute(createContext(contextHolder));
        }

        @Override
        public StateMachineContextHolder getContextHolder() {
            return contextHolder;
        }
    }
}
