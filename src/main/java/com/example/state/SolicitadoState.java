package com.example.state;

import com.example.model.Credito;

public class SolicitadoState implements CreditoState {
    @Override
    public String nombre() {
        return "SOLICITADO";
    }

    @Override
    public void aprobar(Credito c) {
        c.setState(new AprobadoState());
    }

    @Override
    public void rechazar(Credito c, String motivo) {
        c.setState(new RechazadoState());
    }
}