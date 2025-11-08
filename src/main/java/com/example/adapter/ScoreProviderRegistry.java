package com.example.adapter;

import com.example.model.Cliente;
import com.example.model.Score;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
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