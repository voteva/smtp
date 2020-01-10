package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.ChannelsContext;
import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.utils.SocketUtils.writeToChannel;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class FinalAction
        implements Action {
    private static final Logger logger = getLogger(FinalAction.class);

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute FINALIZE action for '{}'", contextHolder.getMxRecord());

            writeToChannel(contextHolder.getSelectionKey(), "RSET");

            contextHolder.getSelectionKey().cancel();
            contextHolder.getSelectionKey().channel().close();

            ChannelsContext.instance().setChannelReady(contextHolder.getDomain());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
