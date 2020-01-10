package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.common.logger.Logger;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import static com.bmstu.nets.client.statemachine.Event.FINALIZE;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

class ConnectAction
        implements Action {
    private static final Logger logger = getLogger(ConnectAction.class);

    private static final int DEFAULT_SOCKET_PORT = 25;

    @Override
    public void execute(StateMachineContext context) {
        try {
            final StateMachineContextHolder contextHolder = context.getContextHolder();

            logger.debug("Execute CONNECT action for '{}'", contextHolder.getMxRecord());

            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);

            logger.debug("Trying to connect to '{}'...", contextHolder.getMxRecord());
            channel.connect(new InetSocketAddress(contextHolder.getMxRecord(), DEFAULT_SOCKET_PORT));

            SelectionKey selectionKey = channel.register(contextHolder.getSelector(), SelectionKey.OP_CONNECT);
            selectionKey.attach(context);
            contextHolder.setSelectionKey(selectionKey);

        } catch (Exception e) {
            logger.error(e.getMessage());
            context.raise(FINALIZE, ANY);
        }
    }
}
