package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.MAIL_FROM;
import static com.bmstu.nets.client.statemachine.EventStatus.SUCCESS;
import static com.bmstu.nets.client.utils.SocketUtils.socketRead;
import static com.bmstu.nets.client.utils.SocketUtils.socketWrite;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

class HeloAction
        implements Action {
    private static final Logger logger = getLogger(HeloAction.class);

    private static final String SERVER_NAME = "local.server";

    @Override
    public void execute(StateMachineContext context) {
        final StateMachineContextHolder contextHolder = context.getContextHolder();

        socketWrite(contextHolder.getSocketWriter(), "HELO " + SERVER_NAME);
        int responseStatus = socketRead(contextHolder.getSocketReader());
        if (responseStatus != 250) {
            logger.error("Failed to say HELO to '{}'", contextHolder.getMxRecord());
            return;
        }

        context.raise(MAIL_FROM, SUCCESS);
    }
}
