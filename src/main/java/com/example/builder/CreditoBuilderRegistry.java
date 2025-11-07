package com.example.builder;

import com.example.model.Credito;
import org.springframework.stereotype.Service;

@Service
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