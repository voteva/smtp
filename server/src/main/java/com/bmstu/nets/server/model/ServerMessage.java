package com.bmstu.nets.server.model;

import com.bmstu.nets.common.model.Message;
import com.bmstu.nets.common.model.MessageStatus;
import com.bmstu.nets.server.msg.MessageSaver;

public class ServerMessage extends Message {

    public boolean save() {
        return MessageSaver.save(this);
    }

    public boolean to_new() {
//        TODO remove from dir
        this.status = MessageStatus.NEW;
        return save();
    }
}
