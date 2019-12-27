package com.bmstu.nets.server.model;

import com.bmstu.nets.common.model.Message;
import com.bmstu.nets.common.model.MessageStatus;
import com.bmstu.nets.server.logger.Logger;
import com.bmstu.nets.server.msg.MessageSaver;

import java.io.File;

public class ServerMessage extends Message {
    private static final Logger LOG = new Logger();

    public boolean save() {
        return MessageSaver.save(this);
    }

    public boolean to_new() {
//        TODO remove from dir
        boolean delete = new File(this.getDir()).delete();
        if (!delete) {
            LOG.info("Warning!!! Can't delete file" + this.getDir());
            return false;
        }
        this.status = MessageStatus.NEW;
        return save();
    }
}
