package com.mumbahiz.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidationBeforeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (null != header){
            header = header.trim();
            if (StringUtils.startsWithIgnoreCase(header, "Basic ")){
                byte[] base64tToken = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decode;
                try {
                    decode = Base64.getDecoder().decode(base64tToken);
                    String token = new String(decode, StandardCharsets.UTF_8); // un:pwd
                    int delim = token.indexOf(":");
                    if (delim == -1){
                        throw new BadCredentialsException("Invalid Basic Authentication Token");
                    }
                    String email = token.substring(0, delim);
                    if (email.toLowerCase().contains("test")){
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                } catch (IllegalArgumentException exception){
                    throw new BadCredentialsException("Failed to decode basic authentication token");
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
