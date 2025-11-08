package com.example.factory;

import com.example.model.Cliente;

public class FabricaProductosProvider {

    public ProductoBancarioFactory getFactory(Cliente.TipoCliente tipo) {
        if (tipo == null)
            throw new IllegalArgumentException("Tipo de cliente requerido");
        return switch (tipo) {
            case PERSONA_NATURAL -> new FactoryPersonaNatural();
            case PERSONA_JURIDICA -> new FactoryPersonaJuridica();
            case EXTRANJERO -> new FactoryExtranjero();
        };
    }
}