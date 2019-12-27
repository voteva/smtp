package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.DATA;
import static com.bmstu.nets.client.statemachine.EventStatus.SUCCESS;
import static com.bmstu.nets.client.utils.SocketUtils.socketRead;
import static com.bmstu.nets.client.utils.SocketUtils.socketWrite;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class RcptToAction
        implements Action {
    private static final Logger logger = getLogger(RcptToAction.class);

    @Override
    public void execute(StateMachineContext context) {
        final StateMachineContextHolder contextHolder = context.getContextHolder();

        socketWrite(contextHolder.getSocketWriter(), "RCPT TO: " + contextHolder.getMessage().getTo());
        int responseStatus = socketRead(contextHolder.getSocketReader());
        if (responseStatus != 250) {
            logger.error("Failed to say RCPT TO to '{}'", contextHolder.getMxRecord());
            return;
        }

        context.raise(DATA, SUCCESS);
    }
}
