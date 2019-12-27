package com.bmstu.nets.client.utils;

import com.bmstu.nets.common.logger.Logger;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class SocketUtils {
    private static final Logger logger = getLogger(SocketUtils.class);

    @SneakyThrows(IOException.class)
    public static int socketRead(@Nonnull BufferedReader in) {
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
    public static void socketWrite(@Nonnull BufferedWriter wr, @Nonnull String text) {
        wr.write(text + "\r\n");
        wr.flush();
    }
}
