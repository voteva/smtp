package com.bmstu.nets.client.service;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.client.model.Message;

import javax.annotation.Nonnull;

import static com.bmstu.nets.client.utils.MailUtils.getMxRecord;
import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MessageSenderServiceImpl
        implements MessageSenderService {
    private static final Logger logger = getLogger(MessageSenderServiceImpl.class);

    @Override
    public void sendMessage(@Nonnull Message message) {
//        message.getRecipients().forEach(recipient -> {
//            final String mxRecord = getMxRecord(recipient);
//            if (mxRecord == null) {
//                logger.warn("No mxRecord found for recipient '{}'", recipient);
//            }
//        });
    }
}
