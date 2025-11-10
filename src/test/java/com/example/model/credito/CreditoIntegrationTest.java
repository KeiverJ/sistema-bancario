package com.example.model.credito;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditoIntegrationTest {
    // Prueba integración básica: creación de un objeto Credito
    @Test
    void integracionBasica() {
        Credito credito = new Credito();
        assertNotNull(credito);
    }
}
