package com.example.strategy.impl;

import com.example.strategy.CalculoInteresStrategy;
import com.example.model.Credito;
import com.example.model.Cliente;
import com.example.config.BankConfig;

public class InteresHipotecarioStrategy implements CalculoInteresStrategy {
    @Override
    public double calcularTasaAnual(Credito credito, Cliente cliente, BankConfig config) {
        double base = config.getTasaInteresBase();
        // Hipotecario: más bajo para PN, un ajuste para PJ
        double ajuste = (cliente.getTipoCliente() == Cliente.TipoCliente.PERSONA_JURIDICA) ? -1.0 : -2.0;
        return Math.max(0.0, base + ajuste);
    }
}