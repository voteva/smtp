package com.bmstu.nets.common.model;

import java.time.LocalTime;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Message {
    private String sender;
    private List<String> recipients;
    private String subject;
    private byte[] data;

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    private MessageStatus status;
    private LocalTime created_at;

    public Message() {
        this.recipients = new ArrayList<>();
        this.status = MessageStatus.TMP;
        this.created_at = LocalTime.now();
    }
      
    public Message(String sender,  List<String> recipients, String subject, byte[] data) {
        this.sender = sender;        
        this.recipients = recipients;
        this.subject = subject;
        this.data = data;
        this.status = MessageStatus.TMP;
        this.created_at = LocalTime.now();
    }

    public String getStatusString() {
         switch (status){
            case TMP:
                return "tmp";
            case CUR:
                return "cur";
            case NEW:
                return "new";
            default:
                return "shit";
        }
    }
    public String getDir(){
        return sender + "/Maildir/" + getStatusString() + "/" + created_at.toString();
    }

    public String getSender() {
        return sender;
    }

    public Message setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public Message setRecipients(List<String> recipients) {
        this.recipients = recipients;
        return this;
    }

    public Message addRecipient(String recipient) {
        this.recipients.add(recipient);
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Message setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public Message setData(byte[] data) {
        this.data = data;
        return this;
    }
}
