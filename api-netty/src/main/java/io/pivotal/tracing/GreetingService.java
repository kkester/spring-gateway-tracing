package io.pivotal.tracing;

import io.micrometer.tracing.SpanName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GreetingService {

    @SpanName("Handle Greeting")
    public Mono<String> getGreeting() {
        log.info("Creating greeting");
        return Mono.just("heellllooo");
    }
}
