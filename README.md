# Project Overview

This PoC demonstrates the following architecture, design, and coding strategies specific to spring 3.X:

* Spring Cloud Gateway(SCG) with tracing, Circuit Breaker, and session management persisted by `Redis`.
* Custom Gateway and Global Filters for SCG.
* Spring API applications build with tomcat(See api-web module) and netty(See api-netty module).
* Spring session for both netty and tomcat persisted by `Redis`
* Spring tracing for both netty and tomcat

## Project Modules

### gateway
Module containing SCG application that will route traffic to one of the two backend applications contained in project.
Gateway performs session management, but stores the session with a custom cookie name so as not to collide with downstream applications

### api-netty
Module containing API application built using `webflux` starter

### api-web 
Module containing API application built using `web` starter

## Spring Tracing with Micrometer
Spring sleuth was deprecated in spring 2.X and was replaced by micrometer in spring 3.X.  Enabling tracing varies slightly depending on whether you are implementing it in tomcat vs netty.

```
	implementation 'io.micrometer:micrometer-tracing-bridge-brave'
```

### Netty implementation

```java
	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		SpringApplication.run(SpringApplication.class, args);
	}
```

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.4/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.4/gradle-plugin/reference/html/#build-image)
* [Distributed Tracing Reference Guide](https://micrometer.io/docs/tracing)
* [Getting Started with Distributed Tracing](https://docs.spring.io/spring-boot/docs/3.0.4/reference/html/actuator.html#actuator.micrometer-tracing.getting-started)
* [Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)

### Guides
The following guides illustrate how to use some features concretely:

* [Using Spring Cloud Gateway](https://github.com/spring-cloud-samples/spring-cloud-gateway-sample)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

