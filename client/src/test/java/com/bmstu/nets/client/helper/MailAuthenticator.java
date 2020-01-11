package com.bmstu.nets.client.helper;

import lombok.AllArgsConstructor;

import javax.mail.PasswordAuthentication;

@AllArgsConstructor
public class MailAuthenticator
        extends javax.mail.Authenticator {

    private String login;
    private String password;

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(login, password);
    }
}
