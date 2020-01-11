/*   -*- buffer-read-only: t -*- vi: set ro:
 *
 *  DO NOT EDIT THIS FILE   (client-fsm.h)
 *
 *  It has been AutoGen-ed
 *  From the definitions    client.def
 *  and the template file   fsm
 *
 *  Automated Finite State Machine
 *
 *  Copyright (C) 1992-2018 Bruce Korb - all rights reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name ``Bruce Korb'' nor the name of any other
 *    contributor may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * AutoFSM IS PROVIDED BY Bruce Korb ``AS IS'' AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL Bruce Korb OR ANY OTHER CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/*
 *  This file enumerates the states and transition events for a FSM.
 *
 *  te_smtp_server_fsm_state
 *      The available states.  FSS_INIT is always defined to be zero
 *      and FSS_INVALID and FSS_DONE are always made the last entries.
 *
 *  te_smtp_server_fsm_event
 *      The transition events.  These enumerate the event values used
 *      to select the next state from the current state.
 *      SMTP_SERVER_FSM_EV_INVALID is always defined at the end.
 */
#ifndef AUTOFSM_CLIENT_FSM_H_GUARD
#define AUTOFSM_CLIENT_FSM_H_GUARD 1
/**
 *  Finite State machine States
 *
 *  Count of non-terminal states.  The generated states INVALID and DONE
 *  are terminal, but INIT is not  :-).
 */
#define SMTP_SERVER_FSM_STATE_CT  8
typedef enum {
    SMTP_SERVER_FSM_ST_INIT,
    SMTP_SERVER_FSM_ST_SS_INIT,
    SMTP_SERVER_FSM_ST_SS_WAIT,
    SMTP_SERVER_FSM_ST_SS_MAIL_SET,
    SMTP_SERVER_FSM_ST_SS_RECEPIENTS_SET,
    SMTP_SERVER_FSM_ST_SS_WRITING_DATA,
    SMTP_SERVER_FSM_ST_SS_DELIVERING,
    SMTP_SERVER_FSM_ST_SS_CLOSED,
    SMTP_SERVER_FSM_ST_INVALID,
    SMTP_SERVER_FSM_ST_DONE
} te_smtp_server_fsm_state;

/**
 *  Finite State machine transition Events.
 *
 *  Count of the valid transition events
 */
#define SMTP_SERVER_FSM_EVENT_CT 14
typedef enum {
    SMTP_SERVER_FSM_EV_HANDLE_HELO,
    SMTP_SERVER_FSM_EV_HANDLE_EHLO,
    SMTP_SERVER_FSM_EV_HANDLE_MAIL,
    SMTP_SERVER_FSM_EV_HANDLE_RCPT,
    SMTP_SERVER_FSM_EV_HANDLE_RSET,
    SMTP_SERVER_FSM_EV_HANDLE_DATA,
    SMTP_SERVER_FSM_EV_HANDLE_QUIT,
    SMTP_SERVER_FSM_EV_HANDLE_NOT_IMPLEMENTED,
    SMTP_SERVER_FSM_EV_HANDLE_TEXT,
    SMTP_SERVER_FSM_EV_CONNECTION_ESTABLISHED,
    SMTP_SERVER_FSM_EV_CONNECTION_FAILED,
    SMTP_SERVER_FSM_EV_MESSAGE_SAVED,
    SMTP_SERVER_FSM_EV_TEXT,
    SMTP_SERVER_FSM_EV_CLRF_DOT_CLRF,
    SMTP_SERVER_FSM_EV_INVALID
} te_smtp_server_fsm_event;

/**
 *  Step the FSM.  Returns the resulting state.  If the current state is
 *  SMTP_SERVER_FSM_ST_DONE or SMTP_SERVER_FSM_ST_INVALID, it resets to
 *  SMTP_SERVER_FSM_ST_INIT and returns SMTP_SERVER_FSM_ST_INIT.
 */
extern te_smtp_server_fsm_state
smtp_server_fsm_step(
    te_smtp_server_fsm_state smtp_server_fsm_state,
    te_smtp_server_fsm_event trans_evt,
    const char *cmd,
    void *state );

#endif /* AUTOFSM_CLIENT_FSM_H_GUARD */
/*
 * Local Variables:
 * mode: C
 * c-file-style: "stroustrup"
 * indent-tabs-mode: nil
 * End:
 * end of client-fsm.h */
