package com.example.state;

import com.example.model.Credito;

public interface CreditoState {
    String nombre();

    default void aprobar(Credito c) {
        throw new IllegalStateException("No se puede aprobar en estado " + nombre());
    }

    default void rechazar(Credito c, String motivo) {
        throw new IllegalStateException("No se puede rechazar en estado " + nombre());
    }

    default void desembolsar(Credito c) {
        throw new IllegalStateException("No se puede desembolsar en estado " + nombre());
    }

    default boolean pagar(Credito c, double monto) {
        throw new IllegalStateException("No se puede pagar en estado " + nombre());
    }

    default void marcarMora(Credito c) {
        throw new IllegalStateException("No se puede marcar mora en estado " + nombre());
    }

    default void cerrar(Credito c) {
        throw new IllegalStateException("No se puede cerrar en estado " + nombre());
    }
}