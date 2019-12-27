package com.bmstu.nets.client.scheduler;

import com.bmstu.nets.client.model.Message;
import com.bmstu.nets.client.queue.MessageQueueMap;
import com.bmstu.nets.client.statemachine.ContextHolder;
import com.bmstu.nets.client.statemachine.StateMachine;
import com.bmstu.nets.client.statemachine.StateMachineHolder;
import com.bmstu.nets.common.logger.Logger;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.bmstu.nets.client.statemachine.Event.INIT;
import static com.bmstu.nets.client.statemachine.EventStatus.SUCCESS;
import static com.bmstu.nets.client.utils.MailUtils.getMxRecords;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static java.lang.Thread.sleep;

public class MessageQueueReaderScheduler
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageQueueReaderScheduler.class);

    private static final long DELAY_MILLIS = 2000L;
    private volatile boolean stopped = false;

    private final MessageQueueMap messageQueueMap;
    private final StateMachine stateMachine;

    public MessageQueueReaderScheduler() {
        this.messageQueueMap = MessageQueueMap.instance();
        this.stateMachine = StateMachineHolder.instance().getStateMachine();
    }

    @SneakyThrows
    @Override
    public void run() {
        logger.info("MessageSenderScheduler thread started");
        try {
            while (!stopped) {
                messageQueueMap.getAllDomains()
                        .forEach(domain -> {
                            List<Message> messages = messageQueueMap.getAllForDomain(domain);
                            sendMessages(domain, messages);
                        });

                sleep(DELAY_MILLIS);
            }
        } catch (InterruptedException exception) {
            logger.error("MessageSenderScheduler thread is interrupted");
        }
        logger.info("MessageSenderScheduler thread is stopped");
    }

    @Override
    public void close() {
        stopped = true;
    }

    private void sendMessages(@Nonnull String domain, @Nonnull List<Message> messages) {
        final List<String> mxRecords = getMxRecords(domain);
        if (mxRecords.isEmpty()) {
            logger.warn("No MX records found for domain '{}'", domain);
        }
        final ContextHolder contextHolder = new ContextHolder()
                .setMxRecord(mxRecords.get(0))
                .setMessage(messages.get(0));

        stateMachine.raise(INIT, SUCCESS, contextHolder);
    }

    public void main(String[] args) throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(32000).order(ByteOrder.LITTLE_ENDIAN);

        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress("127.0.0.1", 5555));

        String message = "Hello,Disconnect !";
        boolean exit = false;
        int totalKey = 0;
        Iterator<SelectionKey> iter;
        SelectionKey key;

        for (; ; ) {
            totalKey = selector.select();

            if (exit) {
                selector.close();
                return;
            }

            if (totalKey > 0) {
                iter = selector.selectedKeys().iterator();

                while (iter.hasNext()) {
                    key = iter.next();
                    iter.remove();

                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            //ignore
                        } else if (key.isConnectable()) {
                            SocketChannel sock = (SocketChannel) key.channel();
                            sock.finishConnect();

                            if (sock.isConnected()) {
                                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                            }

                            continue;
                        }

                        if (key.isReadable()) {
                            System.out.println("Интерес на чтение");

                            ReadableByteChannel rc = (ReadableByteChannel) key.channel();

                            int result = rc.read(buffer);

                            if (result > 0) {
                                System.out.println("Читаем !");

                                buffer.flip();

                                StringBuilder builder = new StringBuilder();

                                while (buffer.hasRemaining()) {
                                    builder.append(buffer.getChar());
                                }

                                System.out.println(builder);

                                System.out.println("Закрываем соеденение!");
                                key.channel().close();
                                key.cancel();
                                selector.close();
                                return;
                            } else {
                                System.out.println("Клиент принудительно закрыл соединение! result <= 0");

                                key.channel().close();
                                key.cancel();
                            }
                        }

                        if (key.isWritable()) {
                            try {
                                TimeUnit.SECONDS.sleep(3);
                                System.out.println("Пишем...!");
                                WritableByteChannel wc = (WritableByteChannel) key.channel();

                                for (int i = 0; i < message.length(); i++) {
                                    buffer.putChar(message.charAt(i));
                                }

                                buffer.flip();

                                wc.write(buffer);
                                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                                System.out.println("Закончили писать !");
                                key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                            } finally {
                                System.out.println("Почистим буффер !");
                                buffer.clear();
                            }
                        }
                    }
                }
            }
        }
    }
}
