package com.example.util;

import com.example.model.transacccion.Transaccion;

/**
 * Utilidad para crear datos de prueba para tests.
 */
public class TestDataFactory {
    public static Transaccion transaccion(String id) {
        Transaccion t = new Transaccion();
        t.setId(id);
        t.setDescripcion("Transacción de prueba " + id);
        return t;
    }
}
