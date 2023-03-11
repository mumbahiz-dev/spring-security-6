package com.learn.springsecurity6.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor // will create constructor using any final field
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final  UserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, // our request, we can intercept our request and extract data
            @NonNull HttpServletResponse response, // our response, provide new data within the response (add header)
            @NonNull FilterChain filterChain // is the chain of responsibility design pattern, contains list filter that we need to execute
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        //extract jwt
        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) { // authentication == null . means user not authenticate
            UserDetails userDetails  = this.userDetailService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                ); // object that spring need to update security context holder

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // update security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // calling filter chain, we need always to pass the hand to the next filters to be executed
        filterChain.doFilter(request, response);
    }
}
