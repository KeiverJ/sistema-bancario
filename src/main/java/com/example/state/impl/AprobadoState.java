package com.example.state.impl;

import com.example.model.credito.Credito;
import com.example.state.core.CreditoState;

public class AprobadoState implements CreditoState {
    @Override
    public String nombre() {
        return "APROBADO";
    }

    @Override
    public void desembolsar(Credito c) {
        c.setState(new DesembolsadoState());
    }

    @Override
    public void rechazar(Credito c, String motivo) {
        c.setState(new RechazadoState());
    }
}