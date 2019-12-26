package com.bmstu.nets.client.service;

import com.bmstu.nets.client.model.Message;
import com.bmstu.nets.client.statemachine.StateMachine;
import com.bmstu.nets.client.statemachine.StateMachineBuilder;
import com.bmstu.nets.common.logger.Logger;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import static com.bmstu.nets.client.statemachine.Event.DATA;
import static com.bmstu.nets.client.statemachine.Event.HELO;
import static com.bmstu.nets.client.statemachine.Event.INIT;
import static com.bmstu.nets.client.statemachine.Event.MAIL_FROM;
import static com.bmstu.nets.client.statemachine.Event.QUIT;
import static com.bmstu.nets.client.statemachine.Event.RCPT_TO;
import static com.bmstu.nets.client.statemachine.EventStatus.SUCCESS;
import static com.bmstu.nets.client.utils.MailUtils.getMxRecords;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MessageSenderServiceImpl
        implements MessageSenderService {
    private static final Logger logger = getLogger(MessageSenderServiceImpl.class);

    private static final int DEFAULT_SOCKET_PORT = 25;
    private static final String SERVER_NAME = "local.server";

    private final StateMachine stateMachine = new StateMachineBuilder()
            .when(INIT, SUCCESS).act(context -> {
                say(context.getSocketWriter(), "HELO " + SERVER_NAME);
                int responseStatus = hear(context.getSocketReader());
                if (responseStatus != 250) {
                    //logger.error("Failed to say HELO to '{}'", mxRecord);
                    logger.error("Failed to say HELO to '{}'");
                }
                context.raise(HELO, SUCCESS);
            })
            .when(HELO, SUCCESS).act(context -> {

            })
            .when(MAIL_FROM, SUCCESS).act(context -> {

            })
            .when(RCPT_TO, SUCCESS).act(context -> {

            })
            .when(DATA, SUCCESS).act(context -> {

            })
            .when(QUIT, SUCCESS).act(context -> {

            })
            .build();

    @Override
    public void sendMessages(@Nonnull String domain, @Nonnull List<Message> messages) {
        final List<String> mxRecords = getMxRecords(domain);
        if (mxRecords.isEmpty()) {
            logger.warn("No MX records found for domain '{}'", domain);
        }
        messages.forEach(message -> {
            sendMessage(message, mxRecords);
        });
    }

    private void sendMessage(@Nonnull Message message, @Nonnull List<String> mxRecords) {
        for (String mxRecord : mxRecords) {
            try {
                int responseStatus;

                Socket socket = new Socket(mxRecord, DEFAULT_SOCKET_PORT);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                responseStatus = hear(reader);
                if (responseStatus != 220) {
                    logger.error("Failed to open socket for '{}'", mxRecord);
                    continue;
                }

                say(writer, "HELO " + SERVER_NAME);
                responseStatus = hear(reader);
                if (responseStatus != 250) {
                    logger.error("Failed to say HELO to '{}'", mxRecord);
                    break;
                }

                say(writer, "MAIL FROM: " + message.getFrom());
                responseStatus = hear(reader);
                if (responseStatus != 250) {
                    logger.error("Failed to say MAIL FROM to '{}'", mxRecord);
                    break;
                }

                say(writer, "RCPT TO: " + message.getTo());
                responseStatus = hear(reader);
                if (responseStatus != 250) {
                    logger.error("Failed to say RCPT TO to '{}'", mxRecord);
                    break;
                }

                say(writer, "DATA");
                responseStatus = hear(reader);
                if (responseStatus != 354) {
                    logger.error("Failed to say DATA to '{}'", mxRecord);
                    break;
                }

                say(writer, message.getData());
                responseStatus = hear(reader);
                if (responseStatus != 250) {
                    logger.error("Data content rejected for '{}'", mxRecord);
                    break;
                }

                say(writer, "QUIT");
                responseStatus = hear(reader);
                if (responseStatus != 221) {
                    logger.error("Got error while closing connection [status:" + responseStatus + "]");
                    break;
                }

                reader.close();
                writer.close();
                socket.close();
                return;

            } catch (Exception e) {
                logger.error("Failed to send message to '{}'. Reason: {}", mxRecord, e.getMessage());
            }
        }
    }

    @SneakyThrows(IOException.class)
    private int hear(@Nonnull BufferedReader in) {
        String line;
        int result = 0;

        while ((line = in.readLine()) != null) {
            String prefix = line.substring(0, 3);
            logger.debug(line);
            try {
                result = Integer.parseInt(prefix);
            } catch (Exception e) {
                logger.error("Got error while hearing smtp server: {}", e.getMessage());
                result = -1;
            }
            if (line.charAt(3) != '-') break;
        }

        return result;
    }

    @SneakyThrows(IOException.class)
    private void say(@Nonnull BufferedWriter wr, @Nonnull String text) {
        wr.write(text + "\r\n");
        wr.flush();
    }
}
