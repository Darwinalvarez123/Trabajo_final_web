package com.tienda.unimagtienda.security.error;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class Http403AccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull AccessDeniedException accessDeniedException) 
            throws IOException, ServletException {
        
        response.setStatus(403);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        PrintWriter writer = response.getWriter();
        writer.print("{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"Access denied\"}");
        writer.flush();
    }
}