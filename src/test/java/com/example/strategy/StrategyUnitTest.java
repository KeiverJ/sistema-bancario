package com.example.strategy;

import com.example.config.BankConfig;
import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.strategy.impl.InteresConsumoStrategy;
import com.example.strategy.core.InteresStrategyRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyUnitTest {
    @Test
    @DisplayName("InteresStrategyRegistry: constructor y tasaPara para todos los tipos")
    void registry_tasaPara_casos() {
        InteresStrategyRegistry registry = new InteresStrategyRegistry();
        BankConfig cfg = new BankConfig();
        Cliente cliente = new Cliente();
        Credito credito = new Credito();

        // Caso: tipo null, retorna tasa base
        credito.setTipoCredito(null);
        double tasaBase = registry.tasaPara(credito, cliente, cfg);
        assertEquals(cfg.getTasaInteresBase(), tasaBase, 0.001);

        // Caso: CONSUMO
        credito.setTipoCredito(Credito.TipoCredito.CONSUMO);
        double tasaConsumo = registry.tasaPara(credito, cliente, cfg);
        assertTrue(tasaConsumo > cfg.getTasaInteresBase());

        // Caso: LIBRE_INVERSION
        credito.setTipoCredito(Credito.TipoCredito.LIBRE_INVERSION);
        double tasaLibre = registry.tasaPara(credito, cliente, cfg);
        assertTrue(tasaLibre > cfg.getTasaInteresBase());

        // Caso: HIPOTECARIO
        credito.setTipoCredito(Credito.TipoCredito.HIPOTECARIO);
        double tasaHip = registry.tasaPara(credito, cliente, cfg);
        assertTrue(tasaHip > 0);

        // Caso: VEHICULO (usa consumo por defecto)
        credito.setTipoCredito(Credito.TipoCredito.VEHICULO);
        double tasaVeh = registry.tasaPara(credito, cliente, cfg);
        assertEquals(tasaConsumo, tasaVeh, 0.001);
    }

    // Verifica que InteresConsumoStrategy incrementa la tasa base en 3 puntos
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
