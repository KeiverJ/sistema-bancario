package com.example.strategy;

import com.example.config.BankConfig;
import com.example.model.Cliente;
import com.example.model.Credito;
import com.example.strategy.impl.InteresConsumoStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyUnitTest {

    @Test
    @DisplayName("InteresConsumoStrategy aumenta base en 3 puntos")
    void consumoStrategyIncrementaBase() {
        BankConfig cfg = new BankConfig();
        Credito credito = new Credito();
        Cliente cliente = new Cliente();
        var strat = new InteresConsumoStrategy();
        double tasa = strat.calcularTasaAnual(credito, cliente, cfg);
        assertEquals(cfg.getTasaInteresBase() + 3.0, tasa, 0.001);
    }
}
