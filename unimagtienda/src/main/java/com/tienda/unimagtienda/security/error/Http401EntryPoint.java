package com.tienda.unimagtienda.security.error;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class Http401EntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull AuthenticationException authException) 
            throws IOException, ServletException {
        
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        PrintWriter writer = response.getWriter();
        writer.print("{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Authentication required\"}");
        writer.flush();
    }
}