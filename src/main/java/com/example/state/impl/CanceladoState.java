package com.example.state.impl;

import com.example.model.credito.Credito;
import com.example.state.core.CreditoState;

public class CanceladoState implements CreditoState {
    @Override
    public String nombre() {
        return "CANCELADO";
    }
}