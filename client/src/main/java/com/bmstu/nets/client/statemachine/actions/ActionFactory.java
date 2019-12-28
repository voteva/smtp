package com.bmstu.nets.client.statemachine.actions;

import com.bmstu.nets.client.statemachine.Action;
import com.bmstu.nets.client.statemachine.Event;
import com.bmstu.nets.client.statemachine.Mode;

import javax.annotation.Nonnull;

public class ActionFactory {

    private final ConnectAction connectAction;
    private final ConnectReadAction connectReadAction;
    private final HeloWriteAction heloWriteAction;
    private final HeloReadAction heloReadAction;
    private final MailFromWriteAction mailFromWriteAction;
    private final MailFromReadAction mailFromReadAction;
    private final RcptToWriteAction rcptToWriteAction;
    private final RcptToReadAction rcptToReadAction;
    private final DataRequestWriteAction dataRequestWriteAction;
    private final DataRequestReadAction dataRequestReadAction;
    private final DataWriteAction dataWriteAction;
    private final DataReadAction dataReadAction;
    private final QuitWriteAction quitWriteAction;
    private final QuitReadAction quitReadAction;
    private final FinalAction finalAction;

    public ActionFactory() {
        connectAction = new ConnectAction();
        connectReadAction = new ConnectReadAction();
        heloWriteAction = new HeloWriteAction();
        heloReadAction = new HeloReadAction();
        mailFromWriteAction = new MailFromWriteAction();
        mailFromReadAction = new MailFromReadAction();
        rcptToWriteAction = new RcptToWriteAction();
        rcptToReadAction = new RcptToReadAction();
        dataRequestWriteAction = new DataRequestWriteAction();
        dataRequestReadAction = new DataRequestReadAction();
        dataWriteAction = new DataWriteAction();
        dataReadAction = new DataReadAction();
        quitWriteAction = new QuitWriteAction();
        quitReadAction = new QuitReadAction();
        finalAction = new FinalAction();
    }

    public Action getAction(@Nonnull Event event, @Nonnull Mode mode) {
        switch (event) {
            case CONNECT:
                return mode == Mode.READ ? connectReadAction : connectAction;
            case HELO:
                return mode == Mode.READ ? heloReadAction : heloWriteAction;
            case MAIL_FROM:
                return mode == Mode.READ ? mailFromReadAction : mailFromWriteAction;
            case RCPT_TO:
                return mode == Mode.READ ? rcptToReadAction : rcptToWriteAction;
            case DATA_REQUEST:
                return mode == Mode.READ ? dataRequestReadAction : dataRequestWriteAction;
            case DATA:
                return mode == Mode.READ ? dataReadAction : dataWriteAction;
            case QUIT:
                return mode == Mode.READ ? quitReadAction : quitWriteAction;
            default:
                return finalAction;
        }
    }
}
