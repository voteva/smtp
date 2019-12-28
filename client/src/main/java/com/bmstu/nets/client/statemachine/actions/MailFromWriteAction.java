package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.FINAL;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.client.utils.SocketUtils.writeToChannel;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MailFromWriteAction
        implements Action {
    private static final Logger logger = getLogger(MailFromWriteAction.class);

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute MAIL FROM action for '{}'", contextHolder.getMxRecord());

            writeToChannel(contextHolder.getSelectionKey(), "MAIL FROM: " + contextHolder.getMessages().peek().getFrom());

        } catch (Exception e) {
            logger.error(e.getMessage());
            context.raise(FINAL, ANY);
        }
    }
}
