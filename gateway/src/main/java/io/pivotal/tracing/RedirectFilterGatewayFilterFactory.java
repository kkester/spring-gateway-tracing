package io.pivotal.tracing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedirectFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<RedirectFilterGatewayFilterFactory.Config> {

    public RedirectFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        log.info("Creating RedirectFilter for {}", config);
        return new RedirectFilter(config.getRedirectLocation());
    }

    @Data
    public static class Config {
        private String redirectLocation;
    }

}
