package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/hello")
    @SneakyThrows
    public ResponseEntity<Hello> getHello(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(helloService.getHello());
    }

    @EventListener
    public void contextStartedEvent(ApplicationStartedEvent ctxStartEvt) {
        log.info("a context refreshed event happened");
    }
}
