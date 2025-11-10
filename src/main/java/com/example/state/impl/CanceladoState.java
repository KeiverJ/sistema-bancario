package com.example.state.impl;

import com.example.state.core.CreditoState;

public class CanceladoState implements CreditoState {
    @Override
    public String nombre() {
        return "CANCELADO";
    }
}