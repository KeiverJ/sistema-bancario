package com.example.observer.core;

public interface EventoObserver {
    void onEvento(DomainEvent event);

    boolean soporta(String tipo);
}