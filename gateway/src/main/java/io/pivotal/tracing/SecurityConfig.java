package io.pivotal.tracing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final GatewayConfig gatewayConfig;

    @Value("${application.hostname:}")
    private String hostname;

    private URI location = URI.create("/");

    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    private ServerRequestCache requestCache = new WebSessionServerRequestCache();

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        var requestCache = new WebSessionServerRequestCache();
        http.requestCache(c -> c.requestCache(requestCache))
            .authorizeExchange(exchanges -> exchanges
                .matchers(new GatewayWebExchangeMatcher(gatewayConfig.getSecurity())).permitAll()
                .pathMatchers("/public/hello.html").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2Login()
            .authenticationSuccessHandler((webFilterExchange, authentication) -> {
                ServerHttpRequest request = webFilterExchange.getExchange().getRequest();
                ServerWebExchange exchange = webFilterExchange.getExchange();
                return this.requestCache.getRedirectUri(exchange).defaultIfEmpty(this.location)
                    .flatMap(l -> this.redirectStrategy.sendRedirect(exchange, l));
            });
        return http.build();
    }
}
