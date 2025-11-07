package com.example.strategy.impl;

import com.example.strategy.CalculoInteresStrategy;
import com.example.model.Credito;
import com.example.model.Cliente;
import com.example.config.BankConfig;

public class InteresConsumoStrategy implements CalculoInteresStrategy {
    @Override
    public double calcularTasaAnual(Credito credito, Cliente cliente, BankConfig config) {
        double base = config.getTasaInteresBase();
        // Consumo: un poco más alto
        return base + 3.0;
    }
}