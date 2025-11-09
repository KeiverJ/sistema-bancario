package com.example.factory.transferencia;

import com.example.model.transacccion.Transaccion;

/**
 * Factory Method base para construir Transacciones.
 * Las subclases definen cómo instanciar la transacción concreta.
 */
public abstract class TransaccionFactory {
    protected abstract Transaccion crear();

    public final Transaccion nueva() {
        return crear();
    }
}