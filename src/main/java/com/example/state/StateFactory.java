package com.example.state;

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