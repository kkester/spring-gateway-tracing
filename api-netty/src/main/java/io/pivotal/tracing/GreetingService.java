package io.pivotal.tracing;

import io.micrometer.tracing.SpanName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GreetingService {

    private final WebClient webClient = WebClient.builder()
        .baseUrl("http://localhost:8080/hello-api")
        .build();

    @SpanName("Handle Greeting")
    public Mono<Greeting> getGreeting() {
        return webClient.get()
            .uri("/hello")
            .retrieve()
            .bodyToMono(Hello.class)
            .map(h -> Greeting.builder()
                .type("Hello")
                .hello(h)
                .build());
    }
}
