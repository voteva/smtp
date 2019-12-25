package com.bmstu.nets.client.scheduler;

import com.bmstu.nets.client.service.MessageSenderService;
import com.bmstu.nets.client.service.MessageSenderServiceImpl;
import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.client.model.Message;
import com.bmstu.nets.client.queue.MessageQueueMap;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static java.lang.Thread.sleep;

public class MessageSenderScheduler
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageSenderScheduler.class);

    private static final long DELAY_MILLIS = 1000L;
    private volatile boolean stopped = false;

    private final MessageQueueMap messageQueueMap;
    private final MessageSenderService messageSenderService;

    public MessageSenderScheduler() {
        this.messageQueueMap = MessageQueueMap.instance();
        this.messageSenderService = new MessageSenderServiceImpl();
    }

    @Override
    public void run() {
        logger.info("MessageSenderScheduler thread started");
        try {
            while (!stopped) {
//                Message message = messageQueueMap.dequeue();
//
//                if (message != null) {
//                    messageSenderService.sendMessage(message);
//                }

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
}
