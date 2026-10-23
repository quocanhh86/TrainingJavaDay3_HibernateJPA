package com.example.springhibernatedatajpa.infrastructure.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {

    @Override
    @NullMarked
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.nanoTime());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    @NullMarked
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long durationNs = System.nanoTime() - startTime;
        long durationMs = durationNs / 1_000_000;
        String user = request.getUserPrincipal().getName();
        if (user == null) {
            user = "anonymous";
        }

        log.info(user + " " + durationMs + " " + request.getRequestURI() + " " + request.getMethod() + " " + response.getStatus());

        if (ex != null) {
            log.error(ex.getMessage(), ex);
        }
    }
}
