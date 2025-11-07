package com.example.observer;

import com.example.model.Cuenta;

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