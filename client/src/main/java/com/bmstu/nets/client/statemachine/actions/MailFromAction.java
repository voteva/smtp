package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.ContextHolder;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.statemachine.Event.RCPT_TO;
import static com.bmstu.nets.client.statemachine.EventStatus.SUCCESS;
import static com.bmstu.nets.client.utils.SocketUtils.socketRead;
import static com.bmstu.nets.client.utils.SocketUtils.socketWrite;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MailFromAction
        implements Action {
    private static final Logger logger = getLogger(MailFromAction.class);

    @Override
    public void execute(StateMachineContext context) {
        final ContextHolder contextHolder = context.getContextHolder();

        socketWrite(contextHolder.getSocketWriter(), "MAIL FROM: " + contextHolder.getMessage().getFrom());
        int responseStatus = socketRead(contextHolder.getSocketReader());
        if (responseStatus != 250) {
            logger.error("Failed to say MAIL FROM to '{}'", contextHolder.getMxRecord());
            return;
        }

        context.raise(RCPT_TO, SUCCESS);
    }
}
