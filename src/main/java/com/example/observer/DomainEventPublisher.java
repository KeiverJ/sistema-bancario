package com.example.observer;


import java.util.List;

public class DomainEventPublisher {

    private final List<EventoObserver> observers;

    public DomainEventPublisher(List<EventoObserver> observers) {
        this.observers = observers;
    }

    public void publish(DomainEvent event) {
        for (EventoObserver o : observers) {
            if (o.soporta(event.tipo())) {
                o.onEvento(event);
            }
        }
    }
}