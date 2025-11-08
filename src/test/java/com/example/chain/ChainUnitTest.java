package com.example.chain;

import com.example.config.BankConfig;
import com.example.model.Cliente;
import com.example.model.Credito;
import com.example.model.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit: prueba la cadena sin dependencias externas, usando objetos simples.
 */
class ChainUnitTest {

    @Test
    @DisplayName("Cadena aprueba crédito básico con score alto")
    void cadenaApruebaCreditoBasico() {
        BankConfig config = new BankConfig();
        ApprovalChainBuilder builder = new ApprovalChainBuilder();
        ApprovalHandler chain = builder.build(config);

    Cliente cliente = new Cliente();
    cliente.setScoreActual(800);
    cliente.setNumeroDocumento("123456789"); // requerido por DocumentacionHandler
        Credito credito = new Credito();
        credito.setMonto(5000);
        credito.setPlazoMeses(12);
    Score score = new Score();
    score.setClienteId(cliente.getId());
    score.setValor(cliente.getScoreActual());
    score.setFuente("TEST");

        ApprovalContext ctx = new ApprovalContext(cliente, credito, score, config);
    chain.handle(ctx);
        assertTrue(ctx.isAprobado());
    }
}
