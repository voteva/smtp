package com.bmstu.nets.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Message {
    private String sender;
    private List<String> recipients;
    private String subject;
    private byte[] data;
}
