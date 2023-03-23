package io.pivotal.tracing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class HelloSessionGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        log.info("Creating HelloSession for {}", config);
        return new HelloSession();
    }

    public static class HelloSession implements GatewayFilter {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            return exchange.getSession()
                .map(session -> {
                    session.getAttributes().putIfAbsent("TODO_SESSION", "");
                    session.getAttributes().put("TODO_SESSION", session.getAttribute("TODO_SESSION") + "Hello");
                    log.info("Received request for greeting {}", (Object)session.getAttribute("TODO_SESSION"));
                    return session;
                })
                .then(chain.filter(exchange));
        }
    }

}
