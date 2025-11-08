package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CuentaIntegrationTest {
    @Test
    void integracionBasica() {
        Cuenta cuenta = new Cuenta();
        assertNotNull(cuenta);
    }
}
