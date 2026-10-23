package com.example.springhibernatedatajpa.config;

import com.example.springhibernatedatajpa.common.GlobalConfig;
import com.example.springhibernatedatajpa.common.interceptor.MustChangePasswordInterceptor;
import com.example.springhibernatedatajpa.common.interceptor.RequestLogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MustChangePasswordInterceptor mustChangePasswordInterceptor;
    private final RequestLogInterceptor requestLogInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(GlobalConfig.folderPath);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + uploadPath + "/", "classpath:/static/img/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogInterceptor);

        registry.addInterceptor(mustChangePasswordInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/change-password", "/logout", "/css/**", "/js/**", "/images/**", "/error")
                .order(1);
    }
}
