# Project Overview

This PoC demonstrates the following architecture, design, and coding strategies specific to spring 3.X:

* Spring Cloud Gateway(SCG) with Security, tracing, Circuit Breaker, and session management persisted by `Redis`.
* SCG implemented as a OAuth resource client integrated with `Keycloak` authorization server.
* Custom Gateway and Global Filters for SCG.
* Spring API applications build with tomcat(See api-web module) and netty(See api-netty module).
* Spring session for both netty and tomcat persisted by `Redis`
* Spring tracing for both netty and tomcat

## SCG Architecture
SCG behavior can be customized with the implementation of filters.  SCG has a number of different types of filters.  
The first filters to be executed are `WebFilters` followed then by route filters.  There are two types of Route Filters.  
The first are `GlobalFilters` which are executed for all routes.  The second are `GatewayFilters` which will only be executed if configured for a given route.

## Project Modules

### gateway
Module containing SCG application that will route traffic to one of the two backend applications contained in project.
Gateway performs session management, but stores the session with a custom cookie name so as not to collide with downstream applications.
Gateway acts a resource client.  If the session for a request does not contain an auth token, then the response will redirect the request to the Keycloak login page.

### api-netty
Module containing API application built using `webflux` starter.

### api-web 
Module containing API application built using `web` starter.

## Spring Tracing with Micrometer
Spring sleuth was deprecated in spring 2.X and was replaced by micrometer in spring 3.X.  Enabling tracing varies slightly depending on whether you are implementing it in tomcat vs netty.

```
implementation 'io.micrometer:micrometer-tracing-bridge-brave'
```

### Netty implementation

To get tracing to work in Netty, the following code is needed to enable trace and span ids to be logged.

```java
public static void main(String[] args) {
    Hooks.enableAutomaticContextPropagation();
    SpringApplication.run(SpringApplication.class, args);
}
```

Also, the following configuration is needed in order to include the trace and span ids in the log statements.

```yaml
logging:
  pattern:
    level: '%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]'
```

To make micrometer compatible with older applications still on sleuth, add the following configuration property to the sleuth application.
```yaml
spring:
  sleuth:
    propagation:
      type: w3c,b3
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

