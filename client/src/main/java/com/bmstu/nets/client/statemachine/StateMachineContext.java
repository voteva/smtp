package com.bmstu.nets.client.statemachine;

public interface StateMachineContext {
    void raise(Event event, EventStatus status);
    ContextHolder getContextHolder();
}
