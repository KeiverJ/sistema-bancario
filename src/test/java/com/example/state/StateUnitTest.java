package com.example.state;
import com.example.state.core.CreditoState;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.credito.Credito;
import com.example.state.core.StateFactory;
import com.example.state.impl.AprobadoState;
import com.example.state.impl.CanceladoState;
import com.example.state.impl.DesembolsadoState;
import com.example.state.impl.EnMoraState;
import com.example.state.impl.RechazadoState;
import com.example.state.impl.SolicitadoState;

import static org.junit.jupiter.api.Assertions.*;

class StateUnitTest {

    @Test
    @DisplayName("CreditoState: rechazar lanza excepción por defecto")
    void creditoStateRechazarDefault() {
        CreditoState state = new CreditoState() {
            public String nombre() { return "SOLICITADO"; }
        };
        Credito c = new Credito();
        Exception ex = assertThrows(IllegalStateException.class, () -> state.rechazar(c, "motivo"));
        assertTrue(ex.getMessage().contains("No se puede rechazar"));
    }

    @Test
    @DisplayName("CreditoState: desembolsar lanza excepción por defecto")
    void creditoStateDesembolsarDefault() {
        CreditoState state = new CreditoState() {
            public String nombre() { return "SOLICITADO"; }
        };
        Credito c = new Credito();
        Exception ex = assertThrows(IllegalStateException.class, () -> state.desembolsar(c));
        assertTrue(ex.getMessage().contains("No se puede desembolsar"));
    }

    @Test
    @DisplayName("CreditoState: pagar lanza excepción por defecto")
    void creditoStatePagarDefault() {
        CreditoState state = new CreditoState() {
            public String nombre() { return "SOLICITADO"; }
        };
        Credito c = new Credito();
        Exception ex = assertThrows(IllegalStateException.class, () -> state.pagar(c, 100));
        assertTrue(ex.getMessage().contains("No se puede pagar"));
    }

    @Test
    @DisplayName("CreditoState: marcarMora lanza excepción por defecto")
    void creditoStateMarcarMoraDefault() {
        CreditoState state = new CreditoState() {
            public String nombre() { return "SOLICITADO"; }
        };
        Credito c = new Credito();
        Exception ex = assertThrows(IllegalStateException.class, () -> state.marcarMora(c));
        assertTrue(ex.getMessage().contains("No se puede marcar mora"));
    }

    @Test
    @DisplayName("CreditoState: cerrar lanza excepción por defecto")
    void creditoStateCerrarDefault() {
        CreditoState state = new CreditoState() {
            public String nombre() { return "SOLICITADO"; }
        };
        Credito c = new Credito();
        Exception ex = assertThrows(IllegalStateException.class, () -> state.cerrar(c));
        assertTrue(ex.getMessage().contains("No se puede cerrar"));
    }

    // Verifica que el crédito pasa de SOLICITADO a CANCELADO manualmente
    @Test
    @DisplayName("Crédito pasa de SOLICITADO a CANCELADO manualmente")
    void cambiaEstadoManual() {
        Credito c = new Credito();
        c.setEstadoActual(Credito.EstadoCredito.SOLICITADO);
        c.setEstadoActual(Credito.EstadoCredito.CANCELADO);
        assertEquals(Credito.EstadoCredito.CANCELADO, c.getEstadoActual());
    }

    // Verifica transiciones de estado en SolicitadoState (aprobar y rechazar)
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

    // Verifica transiciones de estado en AprobadoState (desembolsar y rechazar)
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

    // Verifica transiciones de estado en DesembolsadoState (pagar, marcar mora y cerrar)
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
