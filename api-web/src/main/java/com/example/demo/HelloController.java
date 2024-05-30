package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @PostMapping(value = "/hello", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Hello> createMovement(@ParameterObject @ModelAttribute Hello hello, @RequestPart MultipartFile[] files) {
        Hello savedHello = helloService.createHello(hello, List.of(files));
        return new ResponseEntity<>(savedHello, HttpStatus.CREATED);
    }

    @EventListener
    public void contextStartedEvent(ApplicationStartedEvent ctxStartEvt) {
        log.info("a context refreshed event happened");
    }
}
