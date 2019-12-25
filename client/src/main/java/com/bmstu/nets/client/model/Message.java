package com.bmstu.nets.client.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {
    private String from;
    private String to;
    private String data;
}
