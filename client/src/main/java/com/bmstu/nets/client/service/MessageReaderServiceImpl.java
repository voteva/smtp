package com.bmstu.nets.client.service;

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
    private static final String MAIL_DIR = "./Maildir"; // TODO read from config
    private static final String DIR_NEW = "/new";
    private static final String DIR_CUR = "/cur";

    private static final String HEADER_X_ORIGINAL_FROM = "X-Original-From";
    private static final String HEADER_X_ORIGINAL_TO = "X-Original-To";

    @Nonnull
    @Override
    public List<Message> readNextMessages() {
        try {
            final List<Message> messages = newArrayList();
            Files
                    .walk(Paths.get(MAIL_DIR + DIR_NEW), 1)
                    .filter(Files::isRegularFile)
                    .forEach(messagePath -> {
                        messages.addAll(readMessages(messagePath));
                        moveToCurDir(messagePath);
                    });

            logger.debug("Success to read messages from '{}'", MAIL_DIR);
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
        final String newFileName = getOrCreateDir(MAIL_DIR + DIR_CUR) + "/" + messagePath.getFileName();
        if (messagePath.toFile().renameTo(new File(newFileName))) {
            boolean deleted = messagePath.toFile().delete();
            logger.debug("Message '{}' deleted: {}", messagePath, deleted);
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
        final String headersStr = data.split("\n\n")[0];
        final Map<String, String> headers = newHashMap();

        Arrays.stream(headersStr.split("\n"))
                .forEach(it -> {
                    String[] splitHeader = it.split(":");
                    headers.put(splitHeader[0], splitHeader[1].replaceAll(" ", ""));
                });

        return headers;
    }
}
