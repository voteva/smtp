package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.Event;

import javax.annotation.Nonnull;

public class ActionFactory {

    private final InitAction initAction;
    private final HeloAction heloAction;
    private final MailFromAction mailFromAction;
    private final RcptToAction rcptToAction;
    private final DataAction dataAction;
    private final QuitAction quitAction;

    public ActionFactory() {
        initAction = new InitAction();
        heloAction = new HeloAction();
        mailFromAction = new MailFromAction();
        rcptToAction = new RcptToAction();
        dataAction = new DataAction();
        quitAction = new QuitAction();
    }

    public Action getAction(@Nonnull Event event) {
        switch (event) {
            case INIT:
                return initAction;
            case HELO:
                return heloAction;
            case MAIL_FROM:
                return mailFromAction;
            case RCPT_TO:
                return rcptToAction;
            case DATA:
                return dataAction;
            case QUIT:
                return quitAction;
            default:
                return null;
        }
    }
}
