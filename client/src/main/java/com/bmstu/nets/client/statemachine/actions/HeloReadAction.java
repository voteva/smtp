package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.ERROR;
import static com.bmstu.nets.client.statemachine.Event.MAIL_FROM;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.client.utils.SocketUtils.readFromChannel;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

class HeloReadAction
        implements Action {
    private static final Logger logger = getLogger(HeloReadAction.class);

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute HELO READ action for '{}'", contextHolder.getMxRecord());

            int status = readFromChannel(contextHolder.getSelectionKey());

            if (status != 250) {
                logger.error("Failed to say HELO to '{}', status {}",
                        contextHolder.getMxRecord(), status);
                context.raise(ERROR, ANY);
                return;
            }

            contextHolder.setNextEvent(MAIL_FROM);

        } catch (Exception e) {
            logger.error(e.getMessage());
            context.raise(ERROR, ANY);
        }
    }
}
