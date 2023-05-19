package io.pivotal.tracing;

import lombok.AllArgsConstructor;
import org.springframework.http.server.RequestPath;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Collection;

@AllArgsConstructor
public class GatewayWebExchangeMatcher implements ServerWebExchangeMatcher {

    private final Collection<GatewayAppConfig> matchingAppConfigs;

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        RequestPath requestPath = exchange.getRequest().getPath();
        InetSocketAddress hostHeader = exchange.getRequest().getHeaders().getHost();
        return matchingAppConfigs.stream()
            .filter(c -> c.getPath() == null || requestPath.value().matches(c.getPath()))
            .filter(c -> hostHeader == null || c.getHost() == null || hostHeader.getHostString().matches(c.getHost()))
            .findFirst()
            .map(c -> MatchResult.match())
            .orElse(MatchResult.notMatch());
    }
}
