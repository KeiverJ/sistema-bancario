package com.example.adapter.legacy;

import com.example.adapter.score.ScoreProvider;
import com.example.model.score.Score;

public class LegacyRiskApiAdapter implements ScoreProvider {

    private final LegacyRiskApi legacyApi = new LegacyRiskApi();

    @Override
    public Score obtenerScore(String clienteId) {
        int riskLevel = legacyApi.getRiskLevel(clienteId);

        Score score = new Score();
        score.setClienteId(clienteId);

        int valorScore = switch (riskLevel) {
            case 1 -> 800;
            case 2 -> 700;
            case 3 -> 600;
            case 4 -> 500;
            case 5 -> 400;
            default -> 600;
        };

        score.setValor(valorScore);
        score.setFuente("LEGACY_RISK_API");

        return score;
    }
}