package com.example.state.impl;

import com.example.state.core.CreditoState;

public class RechazadoState implements CreditoState {
    @Override
    public String nombre() {
        return "RECHAZADO";
    }
}