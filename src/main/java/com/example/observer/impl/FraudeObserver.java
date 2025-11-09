package com.example.observer.impl;

import com.example.observer.core.DomainEvent;
import com.example.observer.core.EventoObserver;
import com.example.observer.eventos.TransaccionRegistradaEvent;

public class FraudeObserver implements EventoObserver {
    @Override
    public void onEvento(DomainEvent event) {
        TransaccionRegistradaEvent e = (TransaccionRegistradaEvent) event;
        if (e.getTransaccion().getMonto() > 50_000_000) {
            System.out.println("[FRAUDE] Monto alto detectado: " + e.getTransaccion().getMonto());
        }
    }

    @Override
    public boolean soporta(String tipo) {
        return "TRANSACCION_REGISTRADA".equals(tipo);
    }
}