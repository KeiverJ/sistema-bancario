package com.example.model.cuenta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuentaIntegrationTest {
    // Prueba integración básica: creación de un objeto Cuenta
    @Test
    void integracionBasica() {
        Cuenta cuenta = new Cuenta();
        assertNotNull(cuenta);
    }
}
