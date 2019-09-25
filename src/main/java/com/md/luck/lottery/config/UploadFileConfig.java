package com.md.luck.lottery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import javax.xml.crypto.Data;

@Configuration
public class UploadFileConfig {
    @Value("${file.uploadFolder}")
    private String uploadFolder;

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(uploadFolder);
        //文件最大
//        factory.setMaxFileSize("2MB");
        factory.setMaxFileSize(DataSize.ofMegabytes(2*1024*1024));
        // 设置总上传数据总大小
//        factory.setMaxRequestSize("5MB");
        factory.setMaxRequestSize(DataSize.ofMegabytes(5*1024*1024));
        return factory.createMultipartConfig();
    }
}
