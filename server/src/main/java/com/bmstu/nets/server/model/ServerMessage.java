package com.bmstu.nets.server.model;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.server.helper.MessageSaver;

import java.io.File;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class ServerMessage
        extends Message {
    private static final Logger logger = getLogger(ServerMessage.class);

    public boolean save() {
        return MessageSaver.save(this);
    }

    public boolean moveToNew() {
        boolean delete = new File(this.getDir()).delete();
        if (!delete) {
            logger.info("Warning!!! Can't delete file" + this.getDir());
        }
        this.status = MessageStatus.NEW;
        return save();
    }
}
