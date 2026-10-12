package com.example.springhibernatedatajpa.config;

import com.example.springhibernatedatajpa.common.GlobalConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(GlobalConfig.folderPath);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + uploadPath + "/", "classpath:/static/img/");
    }
}
