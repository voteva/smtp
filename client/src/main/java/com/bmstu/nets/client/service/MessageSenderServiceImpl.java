package com.bmstu.nets.client.service;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.client.model.Message;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import static com.bmstu.nets.client.utils.MailUtils.getMxRecords;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MessageSenderServiceImpl
        implements MessageSenderService {
    private static final Logger logger = getLogger(MessageSenderServiceImpl.class);

    private static final int DEFAULT_SOCKET_PORT = 25;

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

    private void sendMessage(@Nonnull Message message, List<String> mxRecords) {
        for (String mxRecord : mxRecords) {
            try {
                int responseStatus;

                Socket socket = new Socket(mxRecord, DEFAULT_SOCKET_PORT);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                responseStatus = hear(reader);
                if (responseStatus != 220) {
                    logger.error("Got error while opening the socket");
                    throw new RuntimeException("Invalid header");
                }
                say(writer, "HELO local.server");

                responseStatus = hear(reader);
                if (responseStatus != 250) {
                    logger.error("Got error while connecting to SMTP port");
                    throw new RuntimeException("Not SMTP");
                }

                // validate the sender address
                say(writer, "MAIL FROM: " + message.getFrom());
                responseStatus = hear(reader);
                if (responseStatus != 250) {
                    logger.error("Got error after MAIL FROM construct");
                    throw new RuntimeException("Sender rejected");
                }

                say(writer, "RCPT TO: " + message.getTo());
                responseStatus = hear(reader);
                if (responseStatus != 250) {
                    logger.error("Got error after RCPT TO construct");
                    throw new RuntimeException("Receiver rejected");
                }

                say(writer, "DATA");
                responseStatus = hear(reader);
                if (responseStatus != 354) {
                    logger.error("Got error after DATA construct");
                    throw new RuntimeException("Data rejected");
                }

                //say(writer, "From: " + message.getFrom().split("@")[0] + " <" + message.getFrom() + ">");
                //say(writer, "To: " + message.getTo().split("@")[0] + " <" + message.getTo() + ">");
                //say(writer, "Content-Type: text/plain");
                //say(writer, "\n");
                //for (String line : message.getData().split("\n")) {
                say(writer, message.getData() + "\r\n");
                //}
                say(writer, ".");
                responseStatus = hear(reader);
                if (responseStatus != 250) {
                    logger.error("Got error while sending mail data [status:" + responseStatus + "]");
                    throw new RuntimeException("Data content rejected: [status=" + responseStatus + "] " + reader.readLine());
                }

                say(writer, "QUIT");
                responseStatus = hear(reader);
                if (responseStatus != 221) {
                    logger.error("Got error while closing connection [status:" + responseStatus + "]");
                    throw new RuntimeException("Address is not isValid!");
                }

                reader.close();
                writer.close();
                socket.close();
                return;

            } catch (Exception e) {
                logger.error("Failed to send message to '{}'. Reason: {}",
                        message.getTo(), e.getMessage());
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
            } catch (Exception ex) {
                logger.error("Got error while hearing smtp server: " + ex.getLocalizedMessage());
                result = -1;
            }
            if (line.charAt(3) != '-') break;
        }

        return result;
    }

    @SneakyThrows(IOException.class)
    private void say(@Nonnull BufferedWriter wr, @Nonnull String text) {
        wr.write(text);
        wr.flush();
    }
}
