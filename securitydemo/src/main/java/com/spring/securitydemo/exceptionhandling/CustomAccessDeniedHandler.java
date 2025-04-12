package com.spring.securitydemo.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setHeader("Access-Denied-Message", "Access Denied");
        final String message = (accessDeniedException != null && accessDeniedException.getMessage() != null)
            ? accessDeniedException.getMessage()
            : "Access Denied";
        final String path = request.getRequestURI();

        String jsonResponse =
                "{\"timestamp\": \"" + currentDateTime + "\", \"status\": " + HttpStatus.FORBIDDEN.value() +
            ", \"error\": \"" + HttpStatus.FORBIDDEN.getReasonPhrase() + "\", \"message\": \"" + message +
            "\", \"path\": \"" + path + "\"}";
        response.getWriter().write(jsonResponse);
    }
}
