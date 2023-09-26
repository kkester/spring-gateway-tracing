package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelloService {
    public Hello getHello() {
        return Hello.builder().message("Hello").timestamp(Instant.now()).build();
    }
}
