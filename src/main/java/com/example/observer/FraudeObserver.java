package com.example.observer;

import com.example.observer.TransaccionRegistradaEvent;
import org.springframework.stereotype.Component;

@Component
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