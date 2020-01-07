package com.bmstu.nets.client.utils;

import com.bmstu.nets.common.logger.Logger;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class SocketUtils {
    private static final Logger logger = getLogger(SocketUtils.class);
    private static final int BUFFER_SIZE = 1024;

    @SneakyThrows(IOException.class)
    public static void writeToChannel(@Nonnull SelectionKey key, @Nonnull String data) {

        data = data.endsWith("\r\n") ? data : data + "\r\n";
        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());

        try {
            WritableByteChannel channel = (WritableByteChannel) key.channel();

            while (buffer.remaining() != 0) {
                channel.write(buffer);
            }

            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);

        } finally {
            buffer.clear();
        }
    }

    @SneakyThrows(IOException.class)
    public static int readFromChannel(@Nonnull SelectionKey key) {
        final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE).order(ByteOrder.LITTLE_ENDIAN);
        try {
            ReadableByteChannel channel = (ReadableByteChannel) key.channel();
            final StringBuilder response = new StringBuilder();

            while (channel.read(buffer) != 0) {
                buffer.flip();
                response.append(new String(buffer.array(), 0, buffer.limit(), StandardCharsets.US_ASCII));
            }

            logger.debug(response.toString());

            key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);

            return getResponseCode(response.toString());

        } finally {
            buffer.clear();
        }
    }

    private static int getResponseCode(@Nonnull String response) {
        try {
            String prefix = response.substring(0, 3);
            return Integer.parseInt(prefix);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return -1;
        }
    }
}
