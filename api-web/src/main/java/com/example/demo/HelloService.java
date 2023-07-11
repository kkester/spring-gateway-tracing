package com.example.demo;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.SpanName;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelloService {

    private final Tracer tracer;

    public Hello getHello() {
        Span continuedSpan = tracer.nextSpan().name("Greet This");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(continuedSpan.start())) {
            log.info("Service is on the case");
            // You can log an event on a span
            continuedSpan.event("taxCalculated");
        } finally {
            continuedSpan.end();
        }
        return Hello.builder().message("Hello").timestamp(Instant.now()).build();
    }
}
