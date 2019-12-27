package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.ContextHolder;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.common.logger.Logger;

import java.io.IOException;

import static com.bmstu.nets.client.utils.SocketUtils.socketRead;
import static com.bmstu.nets.client.utils.SocketUtils.socketWrite;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class QuitAction
        implements Action {
    private static final Logger logger = getLogger(QuitAction.class);

    @Override
    public void execute(StateMachineContext context) {
        final ContextHolder contextHolder = context.getContextHolder();

        socketWrite(contextHolder.getSocketWriter(), "QUIT");
        int responseStatus = socketRead(contextHolder.getSocketReader());
        if (responseStatus != 221) {
            logger.error("Got error while closing connection [status:" + responseStatus + "]");
        }

        try {
            contextHolder.getSocketReader().close();
            contextHolder.getSocketWriter().close();
            contextHolder.getSocket().close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
