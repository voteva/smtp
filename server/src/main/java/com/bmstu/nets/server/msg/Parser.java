/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmstu.nets.server.msg;

import com.bmstu.nets.common.model.Message;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

/**
 *
 * @author patutinaam
 */
public class Parser {
    public static String parseSender(String command) {
        return command.replace("MAIL FROM:","").replaceAll("\\s+","");
    }

    public static String parseRecipient(String command) {
        return command.replace("RCPT TO:","").replaceAll("\\s+","");
    }
}

