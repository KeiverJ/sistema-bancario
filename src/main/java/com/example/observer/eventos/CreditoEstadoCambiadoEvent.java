package com.example.observer.eventos;

import com.example.model.credito.Credito;
import com.example.observer.core.DomainEvent;

public class CreditoEstadoCambiadoEvent extends DomainEvent {
    private final Credito credito;

    public CreditoEstadoCambiadoEvent(Credito credito) {
        this.credito = credito;
    }

    public Credito getCredito() {
        return credito;
    }

    @Override
    public String tipo() {
        return "CREDITO_ESTADO_CAMBIADO";
    }
}