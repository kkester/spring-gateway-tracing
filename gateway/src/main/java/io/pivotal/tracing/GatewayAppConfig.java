package io.pivotal.tracing;

import lombok.Data;

@Data
public class GatewayAppConfig {
    private String id;
    private String path;
    private String host;
}
