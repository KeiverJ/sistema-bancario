package com.example.observer.impl;

import com.example.observer.core.DomainEvent;
import com.example.observer.core.EventoObserver;
import com.example.observer.eventos.CreditoEstadoCambiadoEvent;

public class NotificacionObserver implements EventoObserver {
    @Override
    public void onEvento(DomainEvent event) {
        if (event instanceof CreditoEstadoCambiadoEvent ce) {
            System.out.println("[NOTIFY] Crédito " + ce.getCredito().getId() +
                    " nuevo estado: " + ce.getCredito().getEstadoActual());
        }
    }

    @Override
    public boolean soporta(String tipo) {
        return "CREDITO_ESTADO_CAMBIADO".equals(tipo);
    }
}