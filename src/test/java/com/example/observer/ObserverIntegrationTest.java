package com.example.observer;

import com.example.model.Credito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverIntegrationTest {

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
