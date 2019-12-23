package com.bmstu.nets.common.model;

import lombok.Data;

import java.util.List;

@Data
public class Message {
    private String sender;
    private List<String> recipients;
    private String subject;
    private byte[] data;
}
