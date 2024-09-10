package com.mumbahiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class MbzBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(MbzBankApplication.class, args);
    }

}
