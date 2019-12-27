package com.bmstu.nets.client.statemachine;

public interface StateMachineContext {
    void raise(Event event, Mode status);
    StateMachineContextHolder getContextHolder();
}
