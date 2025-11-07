package com.example.observer;

import com.example.model.Transaccion;

public class TransaccionRegistradaEvent extends DomainEvent {
    private final Transaccion transaccion;

    public TransaccionRegistradaEvent(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    public Transaccion getTransaccion() {
        return transaccion;
    }

    @Override
    public String tipo() {
        return "TRANSACCION_REGISTRADA";
    }
}