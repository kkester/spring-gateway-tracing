package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/greeting")
    public String getGreeting(HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.info("Received request for greeting {}", session.getAttribute("TODO_SESSION"));
        if (session.getAttribute("TODO_SESSION") == null) {
            session.setAttribute("TODO_SESSION", "HELLO");
        } else {
            session.setAttribute("TODO_SESSION", "HELLO" + session.getAttribute("TODO_SESSION"));
        }
        return "heleleleloooo";
    }
}
