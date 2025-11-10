package com.example.observer.impl;

import com.example.observer.core.DomainEvent;
import com.example.observer.core.EventoObserver;

public class LoggingObserver implements EventoObserver {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LoggingObserver.class);
    @Override
    public void onEvento(DomainEvent event) {
    logger.info("[LOG] Evento {} @ {}", event.tipo(), event.getTimestamp());
    }

    @Override
    public boolean soporta(String tipo) {
        return true;
    }
}