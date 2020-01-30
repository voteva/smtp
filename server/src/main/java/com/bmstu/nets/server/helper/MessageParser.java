/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmstu.nets.server.helper;

public class MessageParser {

    public static String parseSender(String command) {
        return command.replace("MAIL FROM:","").replaceAll("\\s+","");
    }

    public static String parseRecipient(String command) {
        return command.replace("RCPT TO:","").replaceAll("\\s+","");
    }
}

