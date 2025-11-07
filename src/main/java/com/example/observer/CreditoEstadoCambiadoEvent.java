package com.example.observer;

import com.example.model.Credito;

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