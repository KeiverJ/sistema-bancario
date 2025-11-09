package com.example.builder.credito;

import com.example.builder.credito.tipos.ConsumoCreditoBuilder;
import com.example.builder.credito.tipos.HipotecarioCreditoBuilder;
import com.example.builder.credito.tipos.LibreInversionCreditoBuilder;
import com.example.builder.credito.tipos.VehiculoCreditoBuilder;
import com.example.model.credito.Credito;

public class CreditoBuilderRegistry {

    public CreditoBuilder get(Credito.TipoCredito tipo) {
        return switch (tipo) {
            case CONSUMO -> new ConsumoCreditoBuilder();
            case LIBRE_INVERSION -> new LibreInversionCreditoBuilder();
            case HIPOTECARIO -> new HipotecarioCreditoBuilder();
            case VEHICULO -> new VehiculoCreditoBuilder();
        };
    }
}