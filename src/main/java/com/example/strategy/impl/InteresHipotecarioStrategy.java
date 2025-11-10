package com.example.strategy.impl;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.strategy.core.CalculoInteresStrategy;
import com.example.config.BankConfig;

public class InteresHipotecarioStrategy implements CalculoInteresStrategy {
    @Override
    public double calcularTasaAnual(Credito credito, Cliente cliente, BankConfig config) {
        double base = config.getTasaInteresBase();
        double ajuste = (cliente.getTipoCliente() == Cliente.TipoCliente.PERSONA_JURIDICA) ? -1.0 : -2.0;
        return Math.max(0.0, base + ajuste);
    }
}