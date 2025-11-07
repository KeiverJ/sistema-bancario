package com.example.state;

public class RechazadoState implements CreditoState {
    @Override
    public String nombre() {
        return "RECHAZADO";
    }
}