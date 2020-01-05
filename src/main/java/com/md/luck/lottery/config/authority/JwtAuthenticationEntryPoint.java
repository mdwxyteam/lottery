package com.md.luck.lottery.config.authority;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        System.out.println("JwtAuthenticationEntryPoint:"+authException.getMessage());

//        response.addHeader("Access-Control-Allo-Origin", "http://localhost:8080");
//        response.addHeader("Access-Control-Allo-Methods", "*");
//        response.addHeader("Access-Control-Max-Age", "100");
//        response.addHeader("Access-Control-Allo-Headers", "Content-Type");
//        response.addHeader("Access-Control-Allo-Credentials", "true");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"没有凭证");

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
    }
}
