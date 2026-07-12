package com.eecs4413.groupe.userservice.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> attributes = super.getErrorAttributes(
                webRequest, options.including(ErrorAttributeOptions.Include.MESSAGE));

        Throwable error = getError(webRequest);
        if (error != null) {
            attributes.put("message", error.getMessage());
        }
        return attributes;
    }
}
