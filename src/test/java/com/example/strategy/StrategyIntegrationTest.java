package com.example.strategy;

import com.example.config.BankConfig;
import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.strategy.core.InteresStrategyRegistry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StrategyIntegrationTest {

    @Test
    @DisplayName("Registry retorna tasa específica según tipo de crédito")
    void registryRetornaTasaSegunTipo() {
        InteresStrategyRegistry registry = new InteresStrategyRegistry();
        BankConfig cfg = new BankConfig();
        Cliente cliente = new Cliente();
        Credito credito = new Credito();
        credito.setTipoCredito(Credito.TipoCredito.CONSUMO);
        double tasa = registry.tasaPara(credito, cliente, cfg);
        assertTrue(tasa > cfg.getTasaInteresBase());
    }
}
