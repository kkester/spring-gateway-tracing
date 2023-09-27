package io.pivotal.tracing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GreetingService {

    private final WebClient webClient;

    public Mono<Greeting> getGreeting() {
        log.info("Initiating hello request");
        return webClient.get()
            .uri("/hello")
            .retrieve()
            .onStatus(httpStatusCode -> !httpStatusCode.is2xxSuccessful(), ClientResponse::createException)
            .bodyToMono(Hello.class)
            .map(h -> Greeting.builder()
                .type("Hello")
                .hello(h)
                .build());
    }
}
