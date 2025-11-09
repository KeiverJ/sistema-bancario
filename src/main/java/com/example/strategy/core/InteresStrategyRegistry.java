package com.example.strategy.core;

import com.example.config.BankConfig;
import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.strategy.impl.InteresConsumoStrategy;
import com.example.strategy.impl.InteresLibreInversionStrategy;
import com.example.strategy.impl.InteresHipotecarioStrategy;

public class InteresStrategyRegistry {

    private final CalculoInteresStrategy consumo = new InteresConsumoStrategy();
    private final CalculoInteresStrategy libre = new InteresLibreInversionStrategy();
    private final CalculoInteresStrategy hipotecario = new InteresHipotecarioStrategy();

    public double tasaPara(Credito credito, Cliente cliente, BankConfig config) {
        if (credito.getTipoCredito() == null)
            return config.getTasaInteresBase();
        return switch (credito.getTipoCredito()) {
            case CONSUMO -> consumo.calcularTasaAnual(credito, cliente, config);
            case LIBRE_INVERSION -> libre.calcularTasaAnual(credito, cliente, config);
            case HIPOTECARIO -> hipotecario.calcularTasaAnual(credito, cliente, config);
            case VEHICULO -> consumo.calcularTasaAnual(credito, cliente, config); // por defecto
        };
    }
}