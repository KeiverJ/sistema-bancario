package com.example.observer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.credito.Credito;
import com.example.observer.core.DomainEventPublisher;
import com.example.observer.eventos.CreditoEstadoCambiadoEvent;
import com.example.observer.impl.FraudeObserver;
import com.example.observer.impl.LoggingObserver;
import com.example.observer.impl.NotificacionObserver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverIntegrationTest {

    // Verifica que el publisher notifica a los observadores que soportan el tipo de evento
    @Test
    @DisplayName("Publisher notifica observadores que soportan el tipo")
    void publisherNotifica() {
        LoggingObserver log = new LoggingObserver();
        NotificacionObserver notif = new NotificacionObserver();
        FraudeObserver fraude = new FraudeObserver();
        DomainEventPublisher publisher = new DomainEventPublisher(List.of(log, notif, fraude));

        Credito credito = new Credito();
        var event = new CreditoEstadoCambiadoEvent(credito);
        assertDoesNotThrow(() -> publisher.publish(event));
    }
}
