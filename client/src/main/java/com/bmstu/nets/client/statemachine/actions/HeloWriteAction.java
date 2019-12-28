package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.Mode;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.FINALIZE;
import static com.bmstu.nets.client.utils.SocketUtils.writeToChannel;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

class HeloWriteAction
        implements Action {
    private static final Logger logger = getLogger(HeloWriteAction.class);

    private static final String SERVER_NAME = "local.server";

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute HELO WRITE action for '{}'", contextHolder.getMxRecord());

            writeToChannel(contextHolder.getSelectionKey(), "HELO " + SERVER_NAME);

        } catch (Exception e) {
            logger.error(e.getMessage());
            context.raise(FINALIZE, Mode.ANY);
        }
    }
}
