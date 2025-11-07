package com.example.adapter;

import java.util.Random;

/**
 * Simula un servicio legado con métodos y nombres diferentes.
 */
public class LegacyRiskApi {

    // Devuelve un entero entre 300 y 900 (simulación)
    public int fetchRiskIndex(String doc) {
        return 300 + new Random().nextInt(601);
    }

    // Fuente textual
    public String sourceName() {
        return "LEGACY-RISK-SERVICE";
    }

    public int getRiskLevel(String clienteId) {
        // Simulación basada en hash del clienteId
        if (clienteId == null || clienteId.isEmpty()) {
            return 3; // riesgo medio por defecto
        }

        // Usar hash para generar nivel consistente por cliente
        int hash = Math.abs(clienteId.hashCode());
        int nivel = (hash % 5) + 1; // 1-5

        // Para testing, forzar nivel bajo (score alto)
        return Math.min(nivel, 2); // retorna 1 o 2 máximo (scores altos)
    }
}
