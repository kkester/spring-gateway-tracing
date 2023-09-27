package io.pivotal.tracing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OidcSecurityConfigurer {
    public void configure(ServerHttpSecurity http) {
        ServerAuthenticationSuccessHandler successHandler = new RedirectServerAuthenticationSuccessHandler();
        ServerAuthenticationFailureHandler failureHandler = new RedirectServerAuthenticationFailureHandler("/login?error");
        http.oauth2Login(oAuth2LoginSpec -> oAuth2LoginSpec.authenticationSuccessHandler((webFilterExchange, authentication) -> {
            log.info("{} has successfully logged in", authentication.getName());
            return successHandler.onAuthenticationSuccess(webFilterExchange, authentication);
        }).authenticationFailureHandler((webFilterExchange, exception) -> {
            log.info("{} has NOT successfully logged in", exception.getMessage());
            return failureHandler.onAuthenticationFailure(webFilterExchange, exception);
        }));
    }
}