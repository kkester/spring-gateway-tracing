package io.pivotal.tracing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class TracingWebfluxApplication {
	public static void main(String[] args) {
		SpringApplication.run(TracingWebfluxApplication.class, args);
	}
}
