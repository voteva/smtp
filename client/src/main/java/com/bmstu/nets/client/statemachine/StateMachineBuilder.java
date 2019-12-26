package com.bmstu.nets.client.statemachine;

import com.google.common.collect.ImmutableTable;
import lombok.Setter;

public class StateMachineBuilder {

    @Setter
    private StateMachine stateMachine;

    private final ImmutableTable.Builder<Event, EventStatus, Action> builder;

    public StateMachineBuilder() {
        builder = ImmutableTable.builder();
    }

    public StateMachine build() {
        return stateMachine.setTable(builder.build());
    }

    public ActionHolder when(Event event, EventStatus status) {
        return action -> {
            builder.put(event, status, action);
            return StateMachineBuilder.this;
        };
    }

    public interface ActionHolder {
        StateMachineBuilder act(Action action);
    }
}
