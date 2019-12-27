package com.bmstu.nets.client.statemachine;

import com.bmstu.nets.client.statemachine.actions.ActionFactory;
import lombok.Getter;

import static com.bmstu.nets.client.statemachine.Event.*;
import static com.bmstu.nets.client.statemachine.Event.QUIT;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.client.statemachine.Mode.READ;
import static com.bmstu.nets.client.statemachine.Mode.WRITE;

public class StateMachineHolder {
    @Getter
    private final StateMachine stateMachine;

    private static final class StateMachineInstanceHolder {
        static final StateMachineHolder INSTANCE = new StateMachineHolder();
    }

    private StateMachineHolder() {
        final ActionFactory actionFactory = new ActionFactory();
        stateMachine = new StateMachineBuilder()
                .when(CONNECT, ANY).act(actionFactory.getAction(CONNECT, ANY))
                .when(CONNECT, READ).act(actionFactory.getAction(CONNECT, READ))

                .when(HELO, WRITE).act(actionFactory.getAction(HELO, WRITE))
                .when(HELO, READ).act(actionFactory.getAction(HELO, READ))

                .when(MAIL_FROM, WRITE).act(actionFactory.getAction(MAIL_FROM, WRITE))
                .when(MAIL_FROM, READ).act(actionFactory.getAction(MAIL_FROM, READ))

                .when(RCPT_TO, WRITE).act(actionFactory.getAction(RCPT_TO, WRITE))
                .when(RCPT_TO, READ).act(actionFactory.getAction(RCPT_TO, READ))

                .when(DATA_REQUEST, WRITE).act(actionFactory.getAction(DATA_REQUEST, WRITE))
                .when(DATA_REQUEST, READ).act(actionFactory.getAction(DATA_REQUEST, READ))

                .when(DATA, WRITE).act(actionFactory.getAction(DATA, WRITE))
                .when(DATA, READ).act(actionFactory.getAction(DATA, READ))

                .when(QUIT, WRITE).act(actionFactory.getAction(QUIT, WRITE))
                .when(QUIT, READ).act(actionFactory.getAction(QUIT, READ))

                .when(ERROR, ANY).act(actionFactory.getAction(ERROR, ANY))
                .build();
    }

    public static StateMachineHolder instance() {
        return StateMachineInstanceHolder.INSTANCE;
    }
}
