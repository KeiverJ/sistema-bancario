package com.example.observer.impl;

import com.example.observer.core.DomainEvent;
import com.example.observer.core.EventoObserver;

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