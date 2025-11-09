package com.example.decorator.core;

public interface ProductoFinancieroComponent {
    String getId();

    String getDescripcion();

    double getCostoMensual(); // costo adicional mensual (seguros, etc.)

    double getBeneficioMensual(); // cashback / retorno
}