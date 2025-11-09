package com.example.observer.eventos;

import com.example.model.cuenta.Cuenta;
import com.example.observer.core.DomainEvent;

public class CuentaSaldoActualizadoEvent extends DomainEvent {
    private final Cuenta cuenta;

    public CuentaSaldoActualizadoEvent(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    @Override
    public String tipo() {
        return "CUENTA_SALDO_ACTUALIZADO";
    }
}