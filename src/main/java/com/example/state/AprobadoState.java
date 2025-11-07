package com.example.state;

import com.example.model.Credito;

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