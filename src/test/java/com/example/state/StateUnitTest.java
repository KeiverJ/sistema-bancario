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
}
