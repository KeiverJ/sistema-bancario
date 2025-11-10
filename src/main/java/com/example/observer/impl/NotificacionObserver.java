package com.example.observer.impl;

import com.example.observer.core.DomainEvent;
import com.example.observer.core.EventoObserver;
import com.example.observer.eventos.CreditoEstadoCambiadoEvent;

public class NotificacionObserver implements EventoObserver {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(NotificacionObserver.class);
    @Override
    public void onEvento(DomainEvent event) {
        if (event instanceof CreditoEstadoCambiadoEvent ce) {
        logger.info("[NOTIFY] Crédito {} nuevo estado: {}", ce.getCredito().getId(), ce.getCredito().getEstadoActual());
        }
    }

    @Override
    public boolean soporta(String tipo) {
        return "CREDITO_ESTADO_CAMBIADO".equals(tipo);
    }
}