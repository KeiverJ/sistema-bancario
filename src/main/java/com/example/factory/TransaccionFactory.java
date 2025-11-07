package com.example.factory;

import com.example.model.Transaccion;

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