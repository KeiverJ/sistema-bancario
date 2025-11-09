package com.example.state.impl;

import com.example.model.credito.Credito;
import com.example.state.core.CreditoState;

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