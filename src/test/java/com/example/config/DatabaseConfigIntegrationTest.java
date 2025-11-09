package com.example.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para DatabaseConfig: validación de conexión simulada.
 */
class DatabaseConfigIntegrationTest {
    @Test
    @DisplayName("Simulación de conexión con parámetros de configuración")
    void simulacionConexion() {
        DatabaseConfig config = new DatabaseConfig();
        String url = config.getUrl();
        String user = config.getUsername();
        String pass = config.getPassword();
        assertTrue(url.startsWith("jdbc"));
        assertFalse(user.isEmpty());
        assertFalse(pass.isEmpty());
    }
}
