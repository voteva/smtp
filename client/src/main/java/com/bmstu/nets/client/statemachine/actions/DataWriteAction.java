package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.FINAL;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.client.utils.SocketUtils.writeToChannel;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class DataWriteAction
        implements Action {
    private static final Logger logger = getLogger(DataWriteAction.class);

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute DATA WRITE action for '{}'", contextHolder.getMxRecord());

            writeToChannel(contextHolder.getSelectionKey(), contextHolder.getMessages().peek().getData());

        } catch (Exception e) {
            logger.error(e.getMessage());
            context.raise(FINAL, ANY);
        }
    }
}
