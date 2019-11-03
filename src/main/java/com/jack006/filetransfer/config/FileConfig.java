package com.jack006.filetransfer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "file")
@Component
public class FileConfig {
    private String  uploadDir;
    private String aesKey;
    private int aesScretLength;
    private String aesIv;
    private String cbcType;
}
