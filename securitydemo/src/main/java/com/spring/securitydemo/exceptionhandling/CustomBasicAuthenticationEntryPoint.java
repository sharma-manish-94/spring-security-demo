package com.spring.securitydemo.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        final String message = (authException != null && authException.getMessage() != null
                ? authException.getMessage()
                : "Unauthorized access");
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = String.format("{error: %s, message: %s}",
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
               message);
        response.getWriter().write(jsonResponse);

    }
}
