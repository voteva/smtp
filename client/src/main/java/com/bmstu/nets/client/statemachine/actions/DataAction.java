package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.ContextHolder;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.QUIT;
import static com.bmstu.nets.client.statemachine.EventStatus.SUCCESS;
import static com.bmstu.nets.client.utils.SocketUtils.socketRead;
import static com.bmstu.nets.client.utils.SocketUtils.socketWrite;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class DataAction
        implements Action {
    private static final Logger logger = getLogger(DataAction.class);

    @Override
    public void execute(StateMachineContext context) {
        final ContextHolder contextHolder = context.getContextHolder();

        socketWrite(contextHolder.getSocketWriter(), "DATA");
        int responseStatus = socketRead(contextHolder.getSocketReader());
        if (responseStatus != 354) {
            logger.error("Failed to say DATA to '{}'", contextHolder.getMxRecord());
            return;
        }

        socketWrite(contextHolder.getSocketWriter(), contextHolder.getMessage().getData());
        responseStatus = socketRead(contextHolder.getSocketReader());
        if (responseStatus != 250) {
            logger.error("Data content rejected for '{}'", contextHolder.getMxRecord());
            return;
        }

        context.raise(QUIT, SUCCESS);
    }
}
