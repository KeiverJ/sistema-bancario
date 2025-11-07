package com.example.builder;

import com.example.model.Credito;

public interface CreditoBuilder {
    CreditoBuilder desdeBase(Credito base);

    CreditoBuilder conSeguroVida(boolean valor);

    CreditoBuilder conSeguroDesempleo(boolean valor);

    CreditoBuilder conGarantia(String garantia);

    CreditoBuilder conCostoApertura(double costo);

    CreditoBuilder conCuotaAdministracion(double cuota);

    Credito build();
}