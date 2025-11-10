package com.example.strategy.impl;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.strategy.core.CalculoInteresStrategy;
import com.example.config.BankConfig;

public class InteresConsumoStrategy implements CalculoInteresStrategy {
    @Override
    public double calcularTasaAnual(Credito credito, Cliente cliente, BankConfig config) {
        double base = config.getTasaInteresBase();
        return base + 3.0;
    }
}