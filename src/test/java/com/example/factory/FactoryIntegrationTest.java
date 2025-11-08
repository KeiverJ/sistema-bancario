package com.example.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryIntegrationTest {

    @Test
    @DisplayName("Validadores de documento validan formatos esperados")
    void validadoresValidanFormato() {
        Validador vCed = new ValidadorCedula();
        Validador vNit = new ValidadorNIT();
        assertTrue(vCed.validar("1234567"));
        assertFalse(vCed.validar("ABC123"));
        assertTrue(vNit.validar("123456789-1"));
        assertFalse(vNit.validar("123456789"));
    }
}
