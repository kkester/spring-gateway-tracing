package io.pivotal.tracing;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
            .servers(List.of(new Server().url("http://localhost:8080/greeting-api").description("Greeting Service Server")))
            .info(new Info().title("Greeting Service"));
    }
}
