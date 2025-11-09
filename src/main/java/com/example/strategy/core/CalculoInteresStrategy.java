package com.example.strategy.core;

import com.example.config.BankConfig;
import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

/**
 * Estrategia para calcular la tasa de interés anual de un crédito
 */
public interface CalculoInteresStrategy {

    /**
     * Calcula la tasa de interés anual basada en el crédito, cliente y
     * configuración del banco
     * 
     * @param credito El crédito para el cual se calcula la tasa
     * @param cliente El cliente solicitante
     * @param config  Configuración del banco
     * @return Tasa de interés anual en porcentaje
     */
    double calcularTasaAnual(Credito credito, Cliente cliente, BankConfig config);
}