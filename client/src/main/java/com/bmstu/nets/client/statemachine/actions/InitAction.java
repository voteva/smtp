package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.ContextHolder;
import com.bmstu.nets.client.statemachine.StateMachineContext;
import com.bmstu.nets.common.logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static com.bmstu.nets.client.statemachine.Event.HELO;
import static com.bmstu.nets.client.statemachine.EventStatus.SUCCESS;
import static com.bmstu.nets.client.utils.SocketUtils.socketRead;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

class InitAction
        implements Action {
    private static final Logger logger = getLogger(InitAction.class);

    private static final int DEFAULT_SOCKET_PORT = 25;

    @Override
    public void execute(StateMachineContext context) {
        try {
            final ContextHolder contextHolder = context.getContextHolder();

            Socket socket = new Socket(contextHolder.getMxRecord(), DEFAULT_SOCKET_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            contextHolder.setSocket(socket);
            contextHolder.setSocketReader(reader);
            contextHolder.setSocketWriter(writer);

            int responseStatus = socketRead(reader);
            if (responseStatus != 220) {
                logger.error("Failed to open socket for '{}'", contextHolder.getMxRecord());
                return;
            }

            context.raise(HELO, SUCCESS);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
