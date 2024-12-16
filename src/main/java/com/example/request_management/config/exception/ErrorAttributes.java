package com.example.request_management.config.exception;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ErrorAttributes  extends DefaultErrorAttributes {
    public final static List<String> criticalAttributes = List.of("message", "path");
//    @Override
//    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
//        final var attrs = super.getErrorAttributes(webRequest, includeStackTrace);
//        criticalAttributes.forEach(attrs::remove);
//        return attrs;
//    }
}
