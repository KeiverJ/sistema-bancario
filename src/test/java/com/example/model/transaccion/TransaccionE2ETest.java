package com.example.model.transaccion;

import org.junit.jupiter.api.Test;

import com.example.model.Transaccion;

import static org.junit.jupiter.api.Assertions.*;

class TransaccionE2ETest {
    @Test
    void flujoCompleto() {
        Transaccion t = new Transaccion();
        t.setMonto(200);
        assertEquals(200, t.getMonto());
    }
}
