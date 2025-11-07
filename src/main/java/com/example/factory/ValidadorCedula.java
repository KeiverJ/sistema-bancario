package com.example.factory;

public class ValidadorCedula implements Validador {
    @Override
    public boolean validar(String documento) {
        return documento != null && documento.matches("\\d{6,10}");
    }

    @Override
    public String tipo() {
        return "CC";
    }
}