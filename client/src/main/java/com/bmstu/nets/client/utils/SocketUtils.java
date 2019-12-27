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

    @SneakyThrows(IOException.class)
    public static void writeToChannel(@Nonnull SelectionKey key, @Nonnull String data) {
        ByteBuffer buffer = ByteBuffer.allocate(32000).order(ByteOrder.LITTLE_ENDIAN); // TODO
        try {
            WritableByteChannel channel = (WritableByteChannel) key.channel();

            data = data.endsWith("\r\n") ? data : data + "\r\n";

            byte[] bytes = data.getBytes();
            channel.write(ByteBuffer.wrap(bytes));

            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
        } finally {
            buffer.clear();
        }
    }

    @SneakyThrows(IOException.class)
    public static int readFromChannel(@Nonnull SelectionKey key) {
        ByteBuffer buffer = ByteBuffer.allocate(32000).order(ByteOrder.LITTLE_ENDIAN); // TODO
        try {
            ReadableByteChannel channel = (ReadableByteChannel) key.channel();

            int result = channel.read(buffer);
            if (result <= 0) {
                return -1;
            }

            buffer.flip();

            final String response = new String(buffer.array(), StandardCharsets.US_ASCII);
            logger.debug(response);

            key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);

            return getResponseCode(response);

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
