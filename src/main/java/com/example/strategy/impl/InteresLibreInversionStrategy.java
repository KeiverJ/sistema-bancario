package com.example.strategy.impl;

import com.example.strategy.CalculoInteresStrategy;
import com.example.model.Credito;
import com.example.model.Cliente;
import com.example.config.BankConfig;

public class InteresLibreInversionStrategy implements CalculoInteresStrategy {
    @Override
    public double calcularTasaAnual(Credito credito, Cliente cliente, BankConfig config) {
        double base = config.getTasaInteresBase();
        // Libre inversión: moderado
        return base + 1.5;
    }
}