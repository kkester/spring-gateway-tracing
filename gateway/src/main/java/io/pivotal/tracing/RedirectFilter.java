package io.pivotal.tracing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

//@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RedirectFilter implements WebFilter {

    private final GatewayWebExchangeMatcher gatewayMatcher;

    public RedirectFilter(GatewayConfig gatewayConfig) {
        this.gatewayMatcher = new GatewayWebExchangeMatcher(gatewayConfig.getTerms());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return gatewayMatcher.matches(exchange)
            .flatMap(m -> m.isMatch() ? chain.filter(exchange) : processExchange(exchange, chain));
    }

    private Mono<Void> processExchange(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        RequestPath requestPath = request.getPath();
        String acceptHeader = request.getHeaders().getFirst(HttpHeaders.ACCEPT);
        log.info("RedirectFilter routing based on {} and {}", acceptHeader, requestPath.value());

        if (!requestPath.value().contains("/oauth2") && !requestPath.value().contains("public/hello.html") && StringUtils.hasText(acceptHeader) && acceptHeader.contains(MediaType.TEXT_HTML_VALUE)) {
            return exchange.getSession()
                .flatMap(webSession -> {
                    if (Boolean.TRUE.equals(webSession.getAttribute("GREETED"))) {
                        return chain.filter(exchange);
                    }
                    webSession.getAttributes().put("GREETED", Boolean.TRUE);
                    final ServerHttpResponse response = exchange.getResponse();
                    response.setRawStatusCode(302);
                    response.getHeaders().set(HttpHeaders.LOCATION, "http://localhost:8080/public/hello.html");
                    return response.setComplete();
                });
        }
        return chain.filter(exchange);
    }

}
