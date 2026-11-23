package com.subastaweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

// La autenticacion es propia (AuthService + sesion), por eso se desactiva el usuario en memoria por defecto.
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@ConfigurationPropertiesScan
public class SubastaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubastaWebApplication.class, args);
    }
}
