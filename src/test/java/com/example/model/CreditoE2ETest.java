package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CreditoE2ETest {
    @Test
    void flujoCompleto() {
        Credito credito = new Credito();
        credito.setMonto(10000);
        credito.setPlazoMeses(24);
        assertEquals(10000, credito.getMonto());
        assertEquals(24, credito.getPlazoMeses());
    }
}
