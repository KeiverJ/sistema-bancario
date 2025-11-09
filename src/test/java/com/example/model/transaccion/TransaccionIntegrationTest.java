package com.example.model.transaccion;

import org.junit.jupiter.api.Test;

import com.example.model.Transaccion;

import static org.junit.jupiter.api.Assertions.*;

class TransaccionIntegrationTest {
    @Test
    void integracionBasica() {
        Transaccion t = new Transaccion();
        assertNotNull(t);
    }
}
