package com.example.observer;

public interface EventoObserver {
    void onEvento(DomainEvent event);

    boolean soporta(String tipo);
}