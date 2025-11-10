package com.example.model.solicitud;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudIntegrationTest {
    // Prueba integración básica: creación de un objeto Solicitud
    @Test
    void integracionBasica() {
        Solicitud solicitud = new Solicitud();
        assertNotNull(solicitud);
    }
}
