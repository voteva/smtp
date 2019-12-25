package com.bmstu.nets.client.scheduler;

import com.bmstu.nets.client.queue.MessageQueueMap;
import com.bmstu.nets.client.service.MessageReaderService;
import com.bmstu.nets.client.service.MessageReaderServiceImpl;
import com.bmstu.nets.common.logger.Logger;

import static com.bmstu.nets.client.utils.MailUtils.getDomainName;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static java.lang.Thread.sleep;

public class MessageReaderScheduler
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageReaderScheduler.class);

    private static final long DELAY_MILLIS = 2000L;
    private volatile boolean stopped = false;

    private final MessageQueueMap messageQueueMap;
    private final MessageReaderService messageReaderService;

    public MessageReaderScheduler() {
        this.messageQueueMap = MessageQueueMap.instance();
        this.messageReaderService = new MessageReaderServiceImpl();
    }

    @Override
    public void run() {
        logger.info("MessageReaderScheduler thread started");
        try {
            while (!stopped) {
                messageReaderService.readNewMessages()
                        .forEach(message -> {
                            messageQueueMap.putForDomain(getDomainName(message.getTo()), message);
                            logger.debug("Success to add message to queue for recipient '{}'", message.getTo());
                        });

                sleep(DELAY_MILLIS);
            }
        } catch (InterruptedException exception) {
            logger.error("MessageReaderScheduler thread is interrupted");
        }
        logger.info("MessageReaderScheduler thread is stopped");
    }

    @Override
    public void close() {
        stopped = true;
    }
}
