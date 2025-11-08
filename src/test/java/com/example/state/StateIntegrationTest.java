package com.example.state;

import com.example.model.Credito;
import com.example.model.Credito.EstadoCredito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateIntegrationTest {

    @Test
    @DisplayName("Pago de cuota reduce saldo y puede cambiar estado en lógica futura")
    void pagoCuotaReduceSaldo() {
        Credito c = new Credito();
        c.setMonto(10000);
        c.setSaldo(10000);
        // Poner el crédito en estado DESEMBOLSADO para permitir pagos
        c.setEstadoActual(EstadoCredito.DESEMBOLSADO);
        boolean ok = c.pagarCuota(500); // método del dominio
        assertTrue(ok);
        assertTrue(c.getSaldo() < 10000);
    }
}
