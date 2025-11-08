package com.example.service.Credito;

import com.example.model.Credito;
import com.example.service.CreditoService;
import com.example.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CreditoService (con mocks).
 */
class CreditoServiceUnitTest {

    @Test
    @DisplayName("consultarSaldo retorna saldo del repo")
    void consultarSaldo_unit() {
        var repo = mock(com.example.repository.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.FabricaProductosProvider.class);
        var interes = mock(com.example.strategy.InteresStrategyRegistry.class);
        var score = mock(com.example.adapter.ScoreProviderRegistry.class);
        var builder = mock(com.example.builder.CreditoBuilderRegistry.class);
        var chain = mock(com.example.chain.ApprovalChainBuilder.class);
        var publisher = mock(com.example.observer.DomainEventPublisher.class);
        var template = mock(com.example.template.SolicitudCreditoTemplate.class);
        CreditoService service = new CreditoService(repo, clienteRepo, config, fabrica, interes, score, builder, chain, publisher, template);
        Credito credito = new Credito();
        credito.setSaldo(1234);
        when(repo.findById("id1")).thenReturn(java.util.Optional.of(credito));
        double saldo = service.consultarSaldo("id1");
        assertEquals(1234, saldo);
    }
}
