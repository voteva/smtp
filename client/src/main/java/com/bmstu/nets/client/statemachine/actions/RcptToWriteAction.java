package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.FINALIZE;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.client.utils.SocketUtils.writeToChannel;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class RcptToWriteAction
        implements Action {
    private static final Logger logger = getLogger(RcptToWriteAction.class);

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute RCPT_TO WRITE action for '{}'", contextHolder.getMxRecord());

            writeToChannel(contextHolder.getSelectionKey(), "RCPT TO: " + contextHolder.getMessages().peek().getTo());

        } catch (Exception e) {
            logger.error(e.getMessage());
            context.raise(FINALIZE, ANY);
        }
    }
}
