package com.example.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para DatabaseConfig: ciclo de uso en flujo de aplicación.
 */
class DatabaseConfigE2ETest {
    @Test
    @DisplayName("Ciclo E2E de obtención de parámetros de conexión")
    void cicloE2EConexion() {
        DatabaseConfig config = new DatabaseConfig();
        assertNotNull(config.getUrl());
        assertNotNull(config.getUsername());
        assertNotNull(config.getPassword());
        assertTrue(config.getUrl().contains("://"));
    }
}
