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
import java.util.Collections;
import java.util.List;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static com.google.common.collect.Lists.newArrayList;

public class MessageReaderServiceImpl
        implements MessageReaderService {
    private static final Logger logger = getLogger(MessageReaderServiceImpl.class);
    private static final String MAIL_DIR = "./Maildir"; // TODO read from config
    private static final String DIR_NEW = "/new";
    private static final String DIR_CUR = "/cur";

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

            // TODO
            Message message = new Message()
                    .setFrom("t.voteva@innopolis.ru")
                    .setTo("tatianavoteva@gmail.com")
                    .setData(messageData);
            return Collections.singletonList(message);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return newArrayList();
        }
    }

    private void moveToCurDir(@Nonnull Path messagePath) {
        final String newFileName = getOrCreateDir(MAIL_DIR + DIR_CUR) + "/" + messagePath.getFileName();
        if (messagePath.toFile().renameTo(new File(newFileName))) {
            boolean deleted = messagePath.toFile().delete();
            logger.debug("Message '{}' deleted: {}", messagePath.toString(), String.valueOf(deleted));
        } else {
            logger.warn("Failed to rename file '{}'", messagePath.getFileName().toString());
        }
    }

    @Nullable
    private String getOrCreateDir(@Nonnull String dirPath) {
        final Path path = Paths.get(dirPath);
        try {
            Files.createDirectories(path);
            return path.toString();
        } catch (IOException e) {
            logger.error("Failed to create directory '{}'", path.toString());
            return null;
        }
    }
}
