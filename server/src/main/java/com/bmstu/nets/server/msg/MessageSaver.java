/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmstu.nets.server.msg;

import com.bmstu.nets.common.model.Message;
import com.bmstu.nets.server.logger.Logger;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author patutinaam
 */
public class MessageSaver {
    private static final Logger LOG = new Logger();

    public static boolean save(Message msg) {
        try {
            FileUtils.writeByteArrayToFile(new File(msg.getDir()), msg.getData());
            return true;
        } catch (IOException e) {
            LOG.info("Warning!!! Can't save file" + msg.getDir());
            return false;
        }

    }
}
