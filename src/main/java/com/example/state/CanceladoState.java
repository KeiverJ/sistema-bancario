package com.example.state;

import com.example.model.Credito;

public class CanceladoState implements CreditoState {
    @Override
    public String nombre() {
        return "CANCELADO";
    }
}