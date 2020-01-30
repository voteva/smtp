package com.bmstu.nets.server.model;

import java.time.LocalTime;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

@Data
@Accessors(chain = true)
public class Message {

    private String sender;
    private List<String> recipients;
    private String subject;
    private byte[] data;
    private LocalTime createdAt;
    protected MessageStatus status;

    public Message() {
        this.recipients = newArrayList();
        this.status = MessageStatus.TMP;
        this.createdAt = LocalTime.now();
    }

    public void addRecipient(String recipient) {
        this.recipients.add(recipient);
    }

    public String getDir() {
        return "./Maildir/" + getStatusString() + "/" + this.getCreatedAt().toString();
    }

    public void setData(String data) {
        String defaultHeader = format(
                "X-Original-From: %s\r\nX-Original-To: %s\r\n",
                this.sender, String.join(", ", this.recipients));

        data = defaultHeader + data;
        this.data = data.getBytes();
    }

    @Nonnull
    private String getStatusString() {
        switch (this.status) {
            case TMP:
                return "tmp";
            case CUR:
                return "cur";
            case NEW:
                return "new";
            default:
                return "";
        }
    }
}
