package com.example.demo;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class Hello {
    private String message;
    private Instant timestamp;
}
