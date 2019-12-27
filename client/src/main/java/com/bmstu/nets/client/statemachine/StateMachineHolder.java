package com.bmstu.nets.client.statemachine;

import com.bmstu.nets.client.statemachine.actions.ActionFactory;
import lombok.Getter;

import static com.bmstu.nets.client.statemachine.Event.*;
import static com.bmstu.nets.client.statemachine.Event.QUIT;
import static com.bmstu.nets.client.statemachine.EventStatus.SUCCESS;

public class StateMachineHolder {
    @Getter
    private final StateMachine stateMachine;

    private static final class StateMachineInstanceHolder {
        static final StateMachineHolder INSTANCE = new StateMachineHolder();
    }

    private StateMachineHolder() {
        final ActionFactory actionFactory = new ActionFactory();
        stateMachine = new StateMachineBuilder()
                .when(INIT, SUCCESS).act(actionFactory.getAction(INIT))
                .when(HELO, SUCCESS).act(actionFactory.getAction(HELO))
                .when(MAIL_FROM, SUCCESS).act(actionFactory.getAction(MAIL_FROM))
                .when(RCPT_TO, SUCCESS).act(actionFactory.getAction(RCPT_TO))
                .when(DATA, SUCCESS).act(actionFactory.getAction(DATA))
                .when(QUIT, SUCCESS).act(actionFactory.getAction(QUIT))
                .build();
    }

    public static StateMachineHolder instance() {
        return StateMachineInstanceHolder.INSTANCE;
    }
}
