package com.example.observer;

import org.springframework.stereotype.Component;

@Component
public class LoggingObserver implements EventoObserver {
    @Override
    public void onEvento(DomainEvent event) {
        System.out.println("[LOG] Evento " + event.tipo() + " @ " + event.getTimestamp());
    }

    @Override
    public boolean soporta(String tipo) {
        return true;
    }
}