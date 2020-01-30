/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmstu.nets.server.helper;

import com.bmstu.nets.common.logger.Logger;
import com.bmstu.nets.server.model.Message;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static com.bmstu.nets.common.logger.LoggerFactory.getLogger;

public class MessageSaver {
    private static final Logger logger = getLogger(MessageSaver.class);

    public static boolean save(Message msg) {
        final String dirName = msg.getDir();
        try {
            FileUtils.writeByteArrayToFile(new File(dirName), msg.getData());
            return true;
        } catch (Exception e) {
            logger.warn("Failed to save file '{}'", dirName);
            return false;
        }
    }
}
