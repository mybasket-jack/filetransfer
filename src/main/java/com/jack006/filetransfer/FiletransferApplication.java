package com.jack006.filetransfer;

import com.jack006.filetransfer.config.FileConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties({
        FileConfig.class
})
public class FiletransferApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FiletransferApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FiletransferApplication.class);
    }
}
