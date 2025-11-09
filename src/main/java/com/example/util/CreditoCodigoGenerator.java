package com.example.util;

import java.util.concurrent.atomic.AtomicInteger;

import com.example.model.cliente.Cliente;

/**
 * Generador de códigos para créditos basado en el tipo de cliente.
 * Mantiene contadores independientes por tipo para consistencia con el patrón
 * Factory.
 */
public class CreditoCodigoGenerator {

    private static final AtomicInteger contadorPersonaNatural = new AtomicInteger(1);
    private static final AtomicInteger contadorPersonaJuridica = new AtomicInteger(2001);
    private static final AtomicInteger contadorExtranjero = new AtomicInteger(3001);

    public static String generarCodigo(Cliente.TipoCliente tipoCliente) {
        int numero = switch (tipoCliente) {
            case PERSONA_NATURAL -> contadorPersonaNatural.getAndIncrement();
            case PERSONA_JURIDICA -> contadorPersonaJuridica.getAndIncrement();
            case EXTRANJERO -> contadorExtranjero.getAndIncrement();
        };
        return "CRE-" + String.format("%04d", numero);
    }
}