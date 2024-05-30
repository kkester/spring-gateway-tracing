package com.example.demo;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

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

    public Hello createHello(Hello hello, List<MultipartFile> helloFiles) {
        log.info("Received {}", hello);
        helloFiles.forEach(f ->  log.info("Processing file {}", f.getOriginalFilename()));
        return hello.toBuilder().timestamp(Instant.now()).build();
    }
}
