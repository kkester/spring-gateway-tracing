package io.pivotal.tracing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
@Slf4j
public class APIGatewayLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var headers = exchange.getRequest().getHeaders().entrySet()
            .stream()
            .toList();

        log.info("Request Headers: {}", headers);
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Response: {} with {}", exchange.getResponse().getStatusCode(), exchange.getResponse().getCookies());
        }));
    }

    @Override
    public int getOrder() {
        return -99;
    }
}
