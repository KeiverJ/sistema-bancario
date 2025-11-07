package com.example.observer;

import java.time.LocalDateTime;

public abstract class DomainEvent {
    private final LocalDateTime timestamp = LocalDateTime.now();

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public abstract String tipo();
}