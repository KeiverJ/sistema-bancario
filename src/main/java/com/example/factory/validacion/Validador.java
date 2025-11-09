package com.example.factory.validacion;

public interface Validador {
    boolean validar(String documento);

    String tipo();
}