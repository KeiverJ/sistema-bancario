package com.example.observer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.observer.core.EventoObserver;
import com.example.observer.eventos.CreditoEstadoCambiadoEvent;
import com.example.observer.eventos.CuentaSaldoActualizadoEvent;
import com.example.observer.eventos.TransaccionRegistradaEvent;
import com.example.observer.impl.FraudeObserver;
import com.example.observer.impl.LoggingObserver;
import com.example.observer.impl.NotificacionObserver;

import static org.junit.jupiter.api.Assertions.*;

class ObserverUnitTest {

    // Verifica que NotificacionObserver solo soporta eventos de crédito y notifica correctamente
    @Test
    @DisplayName("NotificacionObserver solo soporta eventos de crédito y notifica correctamente")
    void notificacionObserverTest() {
        NotificacionObserver obs = new NotificacionObserver();
        var credito = new com.example.model.credito.Credito();
        credito.setId("CRED-1");
        credito.setEstadoActual(com.example.model.credito.Credito.EstadoCredito.APROBADO);
        var event = new CreditoEstadoCambiadoEvent(credito);
        assertTrue(obs.soporta("CREDITO_ESTADO_CAMBIADO"));
        assertFalse(obs.soporta("OTRO_EVENTO"));
        assertDoesNotThrow(() -> obs.onEvento(event));
    }

    // Verifica que FraudeObserver detecta montos altos y solo soporta transacciones
    @Test
    @DisplayName("FraudeObserver detecta montos altos y soporta solo transacciones")
    void fraudeObserverTest() {
        FraudeObserver obs = new FraudeObserver();
        var transaccion = org.mockito.Mockito.mock(com.example.model.transacccion.Transaccion.class);
        org.mockito.Mockito.when(transaccion.getMonto()).thenReturn(60_000_000.0);
        var event = new TransaccionRegistradaEvent(transaccion);
        assertTrue(obs.soporta("TRANSACCION_REGISTRADA"));
        assertFalse(obs.soporta("CREDITO_ESTADO_CAMBIADO"));
        assertDoesNotThrow(() -> obs.onEvento(event));
    }

    // Verifica que LoggingObserver soporta e imprime cualquier evento
    @Test
    @DisplayName("LoggingObserver soporta e imprime cualquier evento")
    void loggingObserverCubreTodo() {
        LoggingObserver obs = new LoggingObserver();
        var event1 = new CreditoEstadoCambiadoEvent(new com.example.model.credito.Credito());
        var event2 = new CuentaSaldoActualizadoEvent(org.mockito.Mockito.mock(com.example.model.cuenta.Cuenta.class));
        var event3 = new TransaccionRegistradaEvent(org.mockito.Mockito.mock(com.example.model.transacccion.Transaccion.class));
        assertTrue(obs.soporta(event1.tipo()));
        assertTrue(obs.soporta(event2.tipo()));
        assertTrue(obs.soporta(event3.tipo()));
        assertDoesNotThrow(() -> obs.onEvento(event1));
        assertDoesNotThrow(() -> obs.onEvento(event2));
        assertDoesNotThrow(() -> obs.onEvento(event3));
    }

    // Verifica que CuentaSaldoActualizadoEvent retorna tipo y cuenta correctamente
    @Test
    @DisplayName("CuentaSaldoActualizadoEvent retorna tipo y cuenta")
    void cuentaSaldoActualizadoEvent() {
        var cuenta = org.mockito.Mockito.mock(com.example.model.cuenta.Cuenta.class);
        var event = new CuentaSaldoActualizadoEvent(cuenta);
        assertEquals("CUENTA_SALDO_ACTUALIZADO", event.tipo());
        assertEquals(cuenta, event.getCuenta());
    }

    @Test
    @DisplayName("TransaccionRegistradaEvent retorna tipo y transacción")
    void transaccionRegistradaEvent() {
        var transaccion = org.mockito.Mockito.mock(com.example.model.transacccion.Transaccion.class);
        var event = new TransaccionRegistradaEvent(transaccion);
        assertEquals("TRANSACCION_REGISTRADA", event.tipo());
        assertEquals(transaccion, event.getTransaccion());
    }

    @Test
    @DisplayName("LoggingObserver soporta cualquier evento")
    void loggingObserverSoportaTodos() {
        LoggingObserver obs = new LoggingObserver();
        // LoggingObserver soporta cualquier tipo
        assertTrue(obs.soporta("CUALQUIER_EVENTO"));
    }

    @Test
    @DisplayName("LoggingObserver imprime evento correctamente")
    void loggingObserverOnEvento() {
        LoggingObserver obs = new LoggingObserver();
        var event = new com.example.observer.eventos.CreditoEstadoCambiadoEvent(new com.example.model.credito.Credito());
        // Solo verificamos que no lanza excepción
        assertDoesNotThrow(() -> obs.onEvento(event));
    }

    @Test
    @DisplayName("DomainEventPublisher publica a observers que soportan el tipo")
    void publisherPublicaSoloSiSoporta() {
        class TestObserver implements EventoObserver {
            boolean recibido = false;
            @Override public void onEvento(com.example.observer.core.DomainEvent event) { recibido = true; }
            @Override public boolean soporta(String tipo) { return "TIPO_OK".equals(tipo); }
        }
        var obs1 = new TestObserver();
        var obs2 = new TestObserver();
        var publisher = new com.example.observer.core.DomainEventPublisher(java.util.List.of(obs1, obs2));
        var event = new com.example.observer.core.DomainEvent() {
            @Override public String tipo() { return "TIPO_OK"; }
        };
        publisher.publish(event);
        assertTrue(obs1.recibido);
        assertTrue(obs2.recibido);
    }

    @Test
    @DisplayName("DomainEventPublisher ignora observers que no soportan el tipo")
    void publisherIgnoraNoSoporta() {
        class TestObserver implements EventoObserver {
            boolean recibido = false;
            @Override public void onEvento(com.example.observer.core.DomainEvent event) { recibido = true; }
            @Override public boolean soporta(String tipo) { return false; }
        }
        var obs = new TestObserver();
        var publisher = new com.example.observer.core.DomainEventPublisher(java.util.List.of(obs));
        var event = new com.example.observer.core.DomainEvent() {
            @Override public String tipo() { return "NINGUNO"; }
        };
        publisher.publish(event);
        assertFalse(obs.recibido);
    }

    @Test
    @DisplayName("CreditoEstadoCambiadoEvent retorna tipo y credito")
    void creditoEstadoCambiadoEvent() {
        var credito = new com.example.model.credito.Credito();
        var event = new com.example.observer.eventos.CreditoEstadoCambiadoEvent(credito);
        assertEquals("CREDITO_ESTADO_CAMBIADO", event.tipo());
        assertEquals(credito, event.getCredito());
    }
}
