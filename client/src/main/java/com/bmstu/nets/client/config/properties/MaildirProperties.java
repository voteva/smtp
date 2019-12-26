package com.bmstu.nets.client.config.properties;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class MaildirProperties {

    @SerializedName("maildir.base-path")
    private String maildirBasePath;

    @SerializedName("maildir.path-new")
    private String maildirPathNew;

    @SerializedName("maildir.path-cur")
    private String maildirPathCur;
}
