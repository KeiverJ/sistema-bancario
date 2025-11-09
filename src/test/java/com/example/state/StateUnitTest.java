package com.example.state;

import com.example.model.Credito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateUnitTest {

    @Test
    @DisplayName("Crédito pasa de SOLICITADO a CANCELADO manualmente")
    void cambiaEstadoManual() {
        Credito c = new Credito();
        c.setEstadoActual(Credito.EstadoCredito.SOLICITADO);
        c.setEstadoActual(Credito.EstadoCredito.CANCELADO);
        assertEquals(Credito.EstadoCredito.CANCELADO, c.getEstadoActual());
    }

    @Test
    @DisplayName("SolicitadoState: aprobar y rechazar cambian de estado")
    void solicitadoStateTransiciones() {
        Credito c = new Credito();
        c.setState(new SolicitadoState());
        c.aprobar();
        assertEquals(Credito.EstadoCredito.APROBADO, c.getEstadoActual());
        c.setState(new SolicitadoState());
        c.rechazar();
        assertEquals(Credito.EstadoCredito.RECHAZADO, c.getEstadoActual());
    }

    @Test
    @DisplayName("AprobadoState: desembolsar y rechazar cambian de estado")
    void aprobadoStateTransiciones() {
        Credito c = new Credito();
        c.setState(new AprobadoState());
        c.desembolsar();
        assertEquals(Credito.EstadoCredito.DESEMBOLSADO, c.getEstadoActual());
        c.setState(new AprobadoState());
        c.rechazar();
        assertEquals(Credito.EstadoCredito.RECHAZADO, c.getEstadoActual());
    }

    @Test
    @DisplayName("DesembolsadoState: pagar, marcar mora y cerrar")
    void desembolsadoStateTransiciones() {
        Credito c = new Credito();
        c.setSaldo(1000);
        c.setState(new DesembolsadoState());
        assertTrue(c.pagarCuota(500));
        assertEquals(500, c.getSaldo());
        c.setState(new DesembolsadoState());
        c.marcarMora();
        assertEquals(Credito.EstadoCredito.EN_MORA, c.getEstadoActual());
        c.setSaldo(0);
        c.setState(new DesembolsadoState());
        c.cerrar();
        assertEquals(Credito.EstadoCredito.CANCELADO, c.getEstadoActual());
        c.setSaldo(100);
        c.setState(new DesembolsadoState());
        Exception ex = assertThrows(IllegalStateException.class, c::cerrar);
        assertTrue(ex.getMessage().contains("saldo > 0"));
    }

    @Test
    @DisplayName("EnMoraState: pagar cambia a Cancelado o Desembolsado")
    void enMoraStateTransiciones() {
        Credito c = new Credito();
        c.setSaldo(100);
        c.setState(new EnMoraState());
        assertTrue(c.pagarCuota(100));
        assertEquals(Credito.EstadoCredito.CANCELADO, c.getEstadoActual());
        c.setSaldo(200);
        c.setState(new EnMoraState());
        assertTrue(c.pagarCuota(50));
        assertEquals(Credito.EstadoCredito.DESEMBOLSADO, c.getEstadoActual());
    }

    @Test
    @DisplayName("CanceladoState y RechazadoState: nombre y métodos prohibidos")
    void canceladoYRechazadoState() {
        Credito c = new Credito();
        c.setState(new CanceladoState());
        assertEquals(Credito.EstadoCredito.CANCELADO, c.getEstadoActual());
        c.setState(new RechazadoState());
        assertEquals(Credito.EstadoCredito.RECHAZADO, c.getEstadoActual());
        Exception ex = assertThrows(IllegalStateException.class, c::aprobar);
        assertTrue(ex.getMessage().contains("No se puede aprobar"));
    }

    @Test
    @DisplayName("StateFactory: from retorna el estado correcto")
    void stateFactoryFrom() {
        assertTrue(StateFactory.from("SOLICITADO") instanceof SolicitadoState);
        assertTrue(StateFactory.from("APROBADO") instanceof AprobadoState);
        assertTrue(StateFactory.from("DESEMBOLSADO") instanceof DesembolsadoState);
        assertTrue(StateFactory.from("EN_MORA") instanceof EnMoraState);
        assertTrue(StateFactory.from("CANCELADO") instanceof CanceladoState);
        assertTrue(StateFactory.from("RECHAZADO") instanceof RechazadoState);
        assertTrue(StateFactory.from(null) instanceof SolicitadoState);
        assertTrue(StateFactory.from("OTRO") instanceof SolicitadoState);
    }
}
