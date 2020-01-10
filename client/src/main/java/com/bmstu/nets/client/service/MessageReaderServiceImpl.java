package com.bmstu.nets.client.service;

import com.bmstu.nets.common.config.PropertiesConfiguration;
import com.bmstu.nets.client.properties.MaildirProperties;
import com.bmstu.nets.client.model.Message;
import com.bmstu.nets.common.logger.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

public class MessageReaderServiceImpl
        implements MessageReaderService {
    private static final Logger logger = getLogger(MessageReaderServiceImpl.class);

    private static final String HEADER_X_ORIGINAL_FROM = "X-Original-From";
    private static final String HEADER_X_ORIGINAL_TO = "X-Original-To";

    private final MaildirProperties properties;

    public MessageReaderServiceImpl() {
        properties = PropertiesConfiguration.instance().loadProperties(MaildirProperties.class);
    }

    @Nonnull
    @Override
    public List<Message> readNewMessages() {
        try {
            final List<Message> messages = newArrayList();
            Files
                    .walk(Paths.get(properties.getMaildirBasePath() + properties.getMaildirPathNew()), 1)
                    .filter(Files::isRegularFile)
                    .forEach(messagePath -> {
                        messages.addAll(readMessages(messagePath));
                        moveToCurDir(messagePath);
                    });

            if (logger.isDebugEnabled() && !messages.isEmpty()) {
                logger.debug("Success to read messages from '{}'. Total messages count={}",
                        properties.getMaildirBasePath(), messages.size());
            }
            return messages;

        } catch (Exception e) {
            logger.error(e.getMessage());
            return newArrayList();
        }
    }

    @Nonnull
    private List<Message> readMessages(@Nonnull Path messagePath) {
        try {
            final String messageData = new String(Files.readAllBytes(Paths.get(messagePath.toString())));
            final Map<String, String> headers = getHeaders(messageData);
            final String originalFrom = headers.get(HEADER_X_ORIGINAL_FROM);
            final String[] originalToArray = headers.get(HEADER_X_ORIGINAL_TO).split(",");

            return Arrays
                    .stream(originalToArray)
                    .map(originalTo -> new Message()
                            .setFrom(originalFrom)
                            .setTo(originalTo)
                            .setData(messageData))
                    .collect(toList());

        } catch (Exception e) {
            logger.warn(e.getMessage());
            return newArrayList();
        }
    }

    private void moveToCurDir(@Nonnull Path messagePath) {
        final String newFileName = getOrCreateDir(
                properties.getMaildirBasePath() +
                        properties.getMaildirPathCur()) + "/" + messagePath.getFileName();

        if (messagePath.toFile().renameTo(new File(newFileName))) {
            logger.debug("Message '{}' moved to cur", messagePath);
        } else {
            logger.warn("Failed to rename file '{}'", messagePath.getFileName());
        }
    }

    @Nullable
    private String getOrCreateDir(@Nonnull String dirPath) {
        final Path path = Paths.get(dirPath);
        try {
            Files.createDirectories(path);
            return path.toString();
        } catch (IOException e) {
            logger.error("Failed to create directory '{}'", path);
            return null;
        }
    }

    @Nonnull
    private Map<String, String> getHeaders(@Nonnull String data) {
        final String headersStr = data.split("\r\n\r\n")[0];
        final Map<String, String> headers = newHashMap();

        Arrays.stream(headersStr.split("\r\n"))
                .forEach(it -> {
                    String[] splitHeader = it.split(":");
                    headers.put(splitHeader[0], splitHeader[1].replaceAll(" ", ""));
                });

        return headers;
    }
}
