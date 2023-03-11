package com.learn.springsecurity6.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // @Configuration  and @EnableWebSecurity  need to be together when we work with springboot 3.0
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    //
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**") //want to whitelist this list
                .permitAll() // permit all whitelist without token
                .anyRequest() // but other endpoint
                .authenticated() // need to authenticated
                .and()
                .sessionManagement() // means what we said that when we implement the filter
                                    // every request should be authenticated, this means that we should not store
                                    // the authentication state or the session State should not be stored
                                    // this help us ensure that each request should be authenticated
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Spring will create new session every request
                .and()
                .authenticationProvider(authenticationProvider) // need to tell spring which authentication provider that i want to use
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // execute this filter before the filter called username and password authentication filter

        return http.build();
    }
}
