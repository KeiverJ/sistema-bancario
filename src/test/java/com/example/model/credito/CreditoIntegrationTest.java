package com.example.model.credito;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditoIntegrationTest {
    @Test
    void integracionBasica() {
        Credito credito = new Credito();
        assertNotNull(credito);
    }
}
