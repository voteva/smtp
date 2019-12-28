package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.FINAL;
import static com.bmstu.nets.client.statemachine.Event.MAIL_FROM;
import static com.bmstu.nets.client.statemachine.Event.QUIT;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.client.utils.SocketUtils.readFromChannel;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class DataReadAction
        implements Action {
    private static final Logger logger = getLogger(DataReadAction.class);

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute DATA READ action for '{}'", contextHolder.getMxRecord());

            int status = readFromChannel(contextHolder.getSelectionKey());

            if (status != 250) {
                logger.error("Data content rejected for '{}', status {}",
                        contextHolder.getMxRecord(), status);
                context.raise(FINAL, ANY);
                return;
            }

            contextHolder.getMessages().poll();

            if (contextHolder.getMessages().isEmpty()) {
                contextHolder.setNextEvent(QUIT);
            } else {
                contextHolder.setNextEvent(MAIL_FROM);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            context.raise(FINAL, ANY);
        }
    }
}