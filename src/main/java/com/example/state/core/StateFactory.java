package com.example.state.core;

import com.example.state.impl.AprobadoState;
import com.example.state.impl.CanceladoState;
import com.example.state.impl.DesembolsadoState;
import com.example.state.impl.EnMoraState;
import com.example.state.impl.RechazadoState;
import com.example.state.impl.SolicitadoState;

public final class StateFactory {
    private StateFactory() {
    }

    public static CreditoState from(String nombre) {
        if (nombre == null)
            return new SolicitadoState();
        return switch (nombre) {
            case "SOLICITADO" -> new SolicitadoState();
            case "APROBADO" -> new AprobadoState();
            case "DESEMBOLSADO" -> new DesembolsadoState();
            case "EN_MORA" -> new EnMoraState();
            case "CANCELADO" -> new CanceladoState();
            case "RECHAZADO" -> new RechazadoState();
            default -> new SolicitadoState();
        };
    }
}