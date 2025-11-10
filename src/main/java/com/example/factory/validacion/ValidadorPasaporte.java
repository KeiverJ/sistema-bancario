package com.example.factory.validacion;

public class ValidadorPasaporte implements Validador {
    @Override
    public boolean validar(String documento) {
        return documento != null && documento.matches("[A-Za-z0-9]{6,12}");
    }

    @Override
    public String tipo() {
        return "PASAPORTE";
    }
}