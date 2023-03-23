package io.pivotal.tracing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GreetingController {

    private final GreetingService greetingService;

    @GetMapping("/greeting")
    Mono<String> getGreeting(WebSession session) {
        session.getAttributes().putIfAbsent("TODO_SESSION", "");
        session.getAttributes().put("TODO_SESSION", session.getAttribute("TODO_SESSION") + "THERE");
        log.info("Received request for greeting {}", (Object)session.getAttribute("TODO_SESSION"));
        return greetingService.getGreeting();
    }
}
