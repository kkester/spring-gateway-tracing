package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/new")
    public String getNew(HttpServletRequest request) {
        log.info("Cookie {}", request.getHeader(HttpHeaders.COOKIE));
        HttpSession session = request.getSession();
        log.info("Received request {} for new {} and {}",
            session.getId(),
            session.getAttribute("NEW_SESSION"),
            session.getAttribute("TODO_SESSION"));
        return "This is new";
    }

    @GetMapping("/greeting")
    public String getGreeting(HttpServletRequest request) {
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
        return helloService.greeting();
    }

    @EventListener
    public void contextStartedEvent(ApplicationStartedEvent ctxStartEvt) {
        log.info("a context refreshed event happened");
    }
}
