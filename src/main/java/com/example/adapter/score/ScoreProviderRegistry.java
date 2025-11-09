package com.example.adapter.score;

import com.example.adapter.buro.BuroFinancieroAdapter;
import com.example.adapter.legacy.LegacyRiskApiAdapter;
import com.example.model.cliente.Cliente;
import com.example.model.score.Score;

import java.util.HashMap;
import java.util.Map;

public class ScoreProviderRegistry {

    private final Map<String, ScoreProvider> providers;

    // ✅ Inyectar los providers como dependencias
    public ScoreProviderRegistry(BuroFinancieroAdapter buroAdapter,
            LegacyRiskApiAdapter legacyAdapter) {
        this.providers = new HashMap<>();
        providers.put("BURO", buroAdapter);
        providers.put("LEGACY", legacyAdapter);
    }

    public Score obtenerScoreParaCliente(Cliente cliente) {
        ScoreProvider provider = providers.get("BURO");
        Score score = provider.obtenerScore(cliente.getId());
        return score;
    }
}