package com.bmstu.nets.common.logger;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LoggerProperties {

    @SerializedName("logger.level")
    private LogLevel level = LogLevel.INFO;

    @SerializedName("logger.file-path")
    private String filePath;
}
