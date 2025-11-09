package com.example.factory.validacion;

public class ValidadorNIT implements Validador {
    @Override
    public boolean validar(String documento) {
        return documento != null && documento.matches("\\d{9}-\\d");
    }

    @Override
    public String tipo() {
        return "NIT";
    }
}