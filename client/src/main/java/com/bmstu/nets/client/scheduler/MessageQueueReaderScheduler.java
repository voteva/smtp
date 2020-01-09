package com.bmstu.nets.client.scheduler;

import com.bmstu.nets.client.ChannelsContext;
import com.bmstu.nets.client.model.Message;
import com.bmstu.nets.client.queue.MessageQueueMap;
import com.bmstu.nets.client.statemachine.StateMachine;
import com.bmstu.nets.client.statemachine.StateMachineContextHolder;
import com.bmstu.nets.client.statemachine.StateMachineHolder;
import com.bmstu.nets.common.logger.Logger;

import javax.annotation.Nonnull;
import java.util.List;

import static com.bmstu.nets.client.statemachine.Event.CONNECT;
import static com.bmstu.nets.client.statemachine.Mode.ANY;
import static com.bmstu.nets.client.utils.MailUtils.getMxRecords;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;
import static com.google.common.collect.Lists.newLinkedList;
import static java.lang.Thread.sleep;

public class MessageQueueReaderScheduler
        implements Runnable, AutoCloseable {
    private static final Logger logger = getLogger(MessageQueueReaderScheduler.class);

    private static final long DELAY_MILLIS = 2000L;
    private volatile boolean stopped = false;

    private final MessageQueueMap messageQueueMap;
    private final ChannelsContext channelsContext;
    private final StateMachine stateMachine;

    public MessageQueueReaderScheduler() {
        this.messageQueueMap = MessageQueueMap.instance();
        this.channelsContext = ChannelsContext.instance();
        this.stateMachine = StateMachineHolder.instance().getStateMachine();
    }

    @Override
    public void run() {
        logger.info("MessageSenderScheduler thread started");
        try {
            while (!stopped) {
                messageQueueMap.getAllDomains()
                        .forEach(domain -> {
                            if (channelsContext.isChannelReady(domain)) {

                                final List<Message> messages = messageQueueMap.getAllForDomain(domain);

                                if (!messages.isEmpty()) {
                                    logger.debug("Retrieve '{}' messages from queue for domain '{}'", messages.size(), domain);

                                    channelsContext.setChannelNotReady(domain);
                                    sendMessages(domain, messages);
                                }
                            }
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
            return;
        }

        final StateMachineContextHolder contextHolder = new StateMachineContextHolder()
                .setSelector(channelsContext.getSelector())
                .setNextEvent(CONNECT)
                .setDomain(domain)
                .setMxRecord(mxRecords.get(0))
                .setMessages(newLinkedList(messages));

        stateMachine.raise(CONNECT, ANY, contextHolder);
    }
}
