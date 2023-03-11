package com.learn.springsecurity6.config;

import com.learn.springsecurity6.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    // this class will hold the app configuration

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        // AuthenticationProvider is the data access object which is responsible to fetch the
        // user details , encode password, and so forth
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // we need to tell this authentication provider which user details service to user
        // in order to fetching information about our user
        authenticationProvider.setUserDetailsService(userDetailsService());
        // need to provide password encoder
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication manger as the name is the one responsible to manage the authentication
    // so the authentication manager have or has a bunch of methods and
    // one of them there is a method that allow us or help us to authenticate user based or using username and password
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // AuthenticationConfiguration hold already information about the AuthenticationManager
        return config.getAuthenticationManager();
    }
}
