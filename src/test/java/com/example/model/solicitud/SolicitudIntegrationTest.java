package com.example.model.solicitud;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudIntegrationTest {
    @Test
    void integracionBasica() {
        Solicitud solicitud = new Solicitud();
        assertNotNull(solicitud);
    }
}
