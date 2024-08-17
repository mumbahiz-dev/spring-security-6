package com.mumbahiz.mbzbank.config;

import com.mumbahiz.mbzbank.exceptionhandling.CustomAccessDeniedHandler;
import com.mumbahiz.mbzbank.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class ProdSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidsession") // Need to create real page for this url
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true) // can't create second session
                        .expiredUrl("/expiredurl"))
                .requiresChannel(rcc-> rcc.anyRequest().requiresSecure()) // Only HTTPS
                .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((requests) ->
                requests.requestMatchers("/my-account", "my-balance", "my-loans", "my-cards").authenticated()
                        .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidsession", "/expiredurl").permitAll()

        );
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                //.accessDeniedPage("/denied") // use it when create UI / application using Spring MVC
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
