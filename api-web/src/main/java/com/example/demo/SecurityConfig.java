package com.example.demo;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain config(HttpSecurity http) {
        return http.authorizeHttpRequests(r -> r.anyRequest().authenticated())
            .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults()))
            .csrf(AbstractHttpConfigurer::disable)
            .build();
    }
}
