package com.example.model.cuenta;

import org.junit.jupiter.api.Test;

import com.example.model.Cuenta;

import static org.junit.jupiter.api.Assertions.*;

class CuentaIntegrationTest {
    @Test
    void integracionBasica() {
        Cuenta cuenta = new Cuenta();
        assertNotNull(cuenta);
    }
}
