package io.pivotal.tracing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, OidcSecurityConfigurer configurer) {
        configurer.configure(http);
        return http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("/hello-api/**").permitAll()
                .anyExchange().authenticated()
            )
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .build();
    }
}
