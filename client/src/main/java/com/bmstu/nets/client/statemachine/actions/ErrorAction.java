package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;

public class ErrorAction
        implements Action {

    @Override
    public void execute(StateMachineContext context) {
        StateMachineContextHolder contextHolder = context.getContextHolder();

        contextHolder.getSelectionKey().cancel();
    }
}
