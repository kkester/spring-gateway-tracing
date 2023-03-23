package io.pivotal.tracing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
public class RedirectFilter implements GatewayFilter {

    private final String redirectLocation;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String acceptHeader = request.getHeaders().getFirst(HttpHeaders.ACCEPT);
        log.info("RedirectFilter routing based on {}", acceptHeader);

        if (StringUtils.hasText(acceptHeader) && acceptHeader.contains(MediaType.TEXT_HTML_VALUE)) {
            final ServerHttpResponse response = exchange.getResponse();
            response.setRawStatusCode(302);
            response.getHeaders().set(HttpHeaders.LOCATION, redirectLocation);
            return response.setComplete();
        }

        return exchange.getSession().doOnSuccess(s -> {}).then(chain.filter(exchange));
    }
}
