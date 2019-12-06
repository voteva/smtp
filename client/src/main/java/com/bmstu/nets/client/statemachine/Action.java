package com.bmstu.nets.client.statemachine;

public interface Action {
    void execute(StateMachineContext context);
}
