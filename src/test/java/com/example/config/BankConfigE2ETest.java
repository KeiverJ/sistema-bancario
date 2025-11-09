package com.example.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para BankConfig: simulación de uso real en operaciones bancarias.
 */
class BankConfigE2ETest {
    @Test
    @DisplayName("Simulación de operación bancaria usando configuración")
    void simulacionOperacionBancaria() {
        BankConfig config = new BankConfig();
        double saldoMin = config.getSaldoMinimo("AHORROS");
        double cuota = config.getCuotaManejo("AHORROS");
        double saldoCliente = saldoMin + 1000;
        double saldoFinal = saldoCliente - cuota;
        assertTrue(saldoFinal > saldoMin);
    }
}
