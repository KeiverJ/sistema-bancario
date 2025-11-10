package com.example.chain;

import com.example.chain.core.ApprovalChainBuilder;
import com.example.chain.core.ApprovalContext;
import com.example.chain.core.ApprovalHandler;
import com.example.config.BankConfig;
import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.model.score.Score;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration: valida rechazo por endeudamiento/plazo/monto ajustando config.
 */
class ChainIntegrationTest {

    // Verifica que la cadena rechaza por monto muy alto respecto al score del cliente
    @Test
    @DisplayName("Cadena rechaza por monto muy alto respecto a score")
    void cadenaRechazaPorMonto() {
        BankConfig config = new BankConfig();
        ApprovalChainBuilder builder = new ApprovalChainBuilder();
        ApprovalHandler chain = builder.build(config);

        Cliente cliente = new Cliente();
        cliente.setScoreActual(550); // score medio
        Credito credito = new Credito();
        credito.setMonto(200_000_000); // monto exagerado
        credito.setPlazoMeses(24);
    Score score = new Score();
    score.setClienteId(cliente.getId());
    score.setValor(cliente.getScoreActual());
    score.setFuente("TEST");

        ApprovalContext ctx = new ApprovalContext(cliente, credito, score, config);
    chain.handle(ctx);
        assertFalse(ctx.isAprobado());
        assertNotNull(ctx.getMotivoRechazo());
    }
}
