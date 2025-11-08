package com.example.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankConfigUnitTest {
    @Test
    @DisplayName("getSaldoMinimo y getCuotaManejo retornan valores por defecto")
    void gettersPorDefecto() {
        BankConfig config = new BankConfig();
        assertTrue(config.getSaldoMinimo("AHORROS") >= 0);
        assertTrue(config.getCuotaManejo("AHORROS") >= 0);
        assertTrue(config.getLimiteDiario() > 0);
    }

}
