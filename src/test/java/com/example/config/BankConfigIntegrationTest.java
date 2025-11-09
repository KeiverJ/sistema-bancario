package com.example.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para BankConfig: validación de límites y reglas de negocio.
 */
class BankConfigIntegrationTest {
    @Test
    @DisplayName("Limite diario y saldo mínimo son coherentes")
    void limitesYReglasNegocio() {
        BankConfig config = new BankConfig();
        double saldoMinAhorros = config.getSaldoMinimo("AHORROS");
        double saldoMinCorriente = config.getSaldoMinimo("CORRIENTE");
        assertTrue(saldoMinAhorros >= 0);
        assertTrue(saldoMinCorriente >= 0);
        assertTrue(config.getLimiteDiario() > saldoMinAhorros);
    }
}
