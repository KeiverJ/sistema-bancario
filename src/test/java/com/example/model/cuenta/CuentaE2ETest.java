package com.example.model.cuenta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuentaE2ETest {
    @Test
    void flujoCompleto() {
        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(1000);
        cuenta.setSaldo(cuenta.getSaldo() - 200);
        assertEquals(800, cuenta.getSaldo());
    }
}
