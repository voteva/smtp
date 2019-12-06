package com.bmstu.nets.client.statemachine;

import com.bmstu.nets.client.model.MessageStatus;
import com.google.common.collect.ImmutableTable;
import lombok.Setter;

public class StateMachineBuilder {

    @Setter
    private StateMachine stateMachine;

    private final ImmutableTable.Builder<MessageStatus, Event, Action> builder;

    public StateMachineBuilder() {
        builder = ImmutableTable.builder();
    }

    public StateMachine build() {
        return stateMachine.setTable(builder.build());
    }

    public ActionHolder when(final MessageStatus status, final Event event) {
        return action -> {
            builder.put(status, event, action);
            return StateMachineBuilder.this;
        };
    }

    public interface ActionHolder {
        StateMachineBuilder act(Action action);
    }
}
