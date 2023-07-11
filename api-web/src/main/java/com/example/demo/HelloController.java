package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/hello")
    @SneakyThrows
    public ResponseEntity<Hello> getHello(HttpServletRequest request) {
        log.info("Cookie {}", request.getHeader(HttpHeaders.COOKIE));
        HttpSession session = request.getSession();
        if (session.getAttribute("TODO_SESSION") == null) {
            session.setAttribute("TODO_SESSION", "HELLO");
        } else {
            session.setAttribute("TODO_SESSION", "HELLO" + session.getAttribute("TODO_SESSION"));
        }
        log.info("Received request {} for greeting {} and {}",
            session.getId(),
            session.getAttribute("NEW_SESSION"),
            session.getAttribute("TODO_SESSION"));
        TimeUnit.MILLISECONDS.sleep(100);
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(300, TimeUnit.SECONDS).mustRevalidate().getHeaderValue())
            .header(HttpHeaders.VARY, "x-batman")
            .header(HttpHeaders.VARY, "x-ironman")
            .body(helloService.getHello());
    }

    @EventListener
    public void contextStartedEvent(ApplicationStartedEvent ctxStartEvt) {
        log.info("a context refreshed event happened");
    }
}
