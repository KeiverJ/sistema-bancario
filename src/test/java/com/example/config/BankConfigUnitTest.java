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

    @Test
    @DisplayName("Setters y getters funcionan y actualizan properties")
    void settersYGetters() {
        BankConfig config = new BankConfig();
        config.setName("NuevoBanco");
        config.setCode("NB-123");
        config.setCountry("Ecuador");
        config.setCurrency("USD");
        config.setTasaInteresBase(10.0);
        config.setScoreMinimo(700);
        config.setMontoMaximoCredito(1000000.0);
        config.setRatioEndeudamientoMaximo(0.5);
        assertEquals("NuevoBanco", config.getName());
        assertEquals("NB-123", config.getCode());
        assertEquals("Ecuador", config.getCountry());
        assertEquals("USD", config.getCurrency());
        assertEquals(10.0, config.getTasaInteresBase());
        assertEquals(700, config.getScoreMinimo());
        assertEquals(1000000.0, config.getMontoMaximoCredito());
        assertEquals(0.5, config.getRatioEndeudamientoMaximo());
        assertEquals("NuevoBanco", config.getProperty("bank.name"));
        assertEquals("NB-123", config.getProperty("bank.code"));
    }

    @Test
    @DisplayName("isMontoValido y isScoreAceptable cubren casos límite")
    void validacionesMontoYScore() {
        BankConfig config = new BankConfig();
        config.setMontoMaximoCredito(1000);
        config.setScoreMinimo(650);
        assertTrue(config.isMontoValido(1000));
        assertFalse(config.isMontoValido(0));
        assertFalse(config.isMontoValido(2000));
        assertTrue(config.isScoreAceptable(650));
        assertFalse(config.isScoreAceptable(600));
    }

    @Test
    @DisplayName("calcularTasaPorScore cubre todas las ramas")
    void calcularTasaPorScore_ramas() {
        BankConfig config = new BankConfig();
        config.setTasaInteresBase(10.0);
        assertEquals(7.0, config.calcularTasaPorScore(800), 0.01);
        assertEquals(8.5, config.calcularTasaPorScore(750), 0.01);
        assertEquals(10.0, config.calcularTasaPorScore(700), 0.01);
        assertEquals(12.0, config.calcularTasaPorScore(650), 0.01);
        assertEquals(15.0, config.calcularTasaPorScore(600), 0.01);
    }

    @Test
    @DisplayName("getProperty y getAllProperties funcionan")
    void propertiesFuncionan() {
        BankConfig config = new BankConfig();
        assertNotNull(config.getAllProperties());
        assertEquals(config.getName(), config.getProperty("bank.name"));
        assertNull(config.getProperty(null));
    }

    @Test
    @DisplayName("getPlazoMaximoCredito y conversiones de propiedades")
    void conversionesYDefaults() {
        BankConfig config = new BankConfig();
        assertEquals(360, config.getPlazoMaximoCredito());
        // Forzar valor inválido
        config.getAllProperties().setProperty("credito.plazo.maximo", "no-num");
        assertEquals(360, config.getPlazoMaximoCredito());
    }

    @Test
    @DisplayName("isValidConfiguration cubre ramas válidas e inválidas")
    void isValidConfiguration_ramas() {
        BankConfig config = new BankConfig();
        assertTrue(config.isValidConfiguration());
        config.setName("");
        assertFalse(config.isValidConfiguration());
        config.setName("Banco");
        config.setCode("");
        assertFalse(config.isValidConfiguration());
        config.setCode("C");
        config.setTasaInteresBase(0);
        assertFalse(config.isValidConfiguration());
        config.setTasaInteresBase(10);
        config.setScoreMinimo(0);
        assertFalse(config.isValidConfiguration());
        config.setScoreMinimo(600);
        config.setMontoMaximoCredito(0);
        assertFalse(config.isValidConfiguration());
        config.setMontoMaximoCredito(1000);
        config.setRatioEndeudamientoMaximo(0);
        assertFalse(config.isValidConfiguration());
        config.setRatioEndeudamientoMaximo(1.1);
        assertFalse(config.isValidConfiguration());
        config.setRatioEndeudamientoMaximo(0.5);
        assertTrue(config.isValidConfiguration());
    }

    @Test
    @DisplayName("toString retorna formato esperado")
    void toString_funciona() {
        BankConfig config = new BankConfig();
        String str = config.toString();
        assertTrue(str.contains("BankConfig"));
        assertTrue(str.contains(config.getName()));
    }

}
