package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.ERROR;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.client.utils.SocketUtils.readFromChannel;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class QuitReadAction
        implements Action {
    private static final Logger logger = getLogger(QuitReadAction.class);

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute QUIT READ action for '{}'", contextHolder.getMxRecord());

            int status = readFromChannel(contextHolder.getSelectionKey());

            if (status != 221) {
                logger.error("Failed to QUIT  from '{}', status {}",
                        contextHolder.getMxRecord(), status);
                context.raise(ERROR, ANY);
                return;
            }

            logger.info("SUCCESS TO SEND EMAIL");

            contextHolder.getSelectionKey().cancel();

        } catch (Exception e) {
            logger.error(e.getMessage());
            context.raise(ERROR, ANY);
        }
    }
}
