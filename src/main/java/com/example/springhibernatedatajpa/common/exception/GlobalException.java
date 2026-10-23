package com.example.springhibernatedatajpa.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
        log.debug("Không tìm thấy tài nguyên tĩnh: {}", ex.getResourcePath());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            NoSuchElementException.class,
            DuplicateResourceException.class
    })
    public String handleBusinessException(RuntimeException ex,
                                          HttpServletRequest request,
                                          RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("businessError", ex.getMessage());
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {
        log.error("Lỗi hệ thống chưa phân loại: ", ex);
        redirectAttributes.addFlashAttribute("businessError", "Lỗi hệ thống: " + ex.getMessage());

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }
}
