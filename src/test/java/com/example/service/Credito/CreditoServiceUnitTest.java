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

    private CreditoService buildService(
            com.example.repository.CreditoRepository repo,
            com.example.repository.ClienteRepository clienteRepo,
            com.example.config.BankConfig config,
            com.example.factory.FabricaProductosProvider fabrica,
            com.example.strategy.InteresStrategyRegistry interes,
            com.example.adapter.ScoreProviderRegistry score,
            com.example.builder.CreditoBuilderRegistry builder,
            com.example.chain.ApprovalChainBuilder chain,
            com.example.observer.DomainEventPublisher publisher,
            com.example.template.SolicitudCreditoTemplate template) {
        return new CreditoService(repo, clienteRepo, config, fabrica, interes, score, builder, chain, publisher, template);
    }

    @Test
    @DisplayName("solicitarCredito lanza excepción si cliente no existe")
    void solicitarCredito_clienteNoExiste() {
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
        when(clienteRepo.findById(anyString())).thenReturn(java.util.Optional.empty());
        CreditoService service = buildService(repo, clienteRepo, config, fabrica, interes, score, builder, chain, publisher, template);
        assertThrows(IllegalArgumentException.class, () ->
            service.solicitarCredito("nope", Credito.TipoCredito.CONSUMO, 1000, 12)
        );
    }

    @Test
    @DisplayName("solicitarCredito llama a template y retorna crédito")
    void solicitarCredito_ok() {
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
        Cliente cliente = new Cliente();
        Credito credito = new Credito();
        when(clienteRepo.findById("cli1")).thenReturn(java.util.Optional.of(cliente));
        when(template.solicitar(any(), any(), anyDouble(), anyInt())).thenReturn(credito);
        CreditoService service = buildService(repo, clienteRepo, config, fabrica, interes, score, builder, chain, publisher, template);
        Credito result = service.solicitarCredito("cli1", Credito.TipoCredito.CONSUMO, 1000, 12);
        assertSame(credito, result);
    }

    @Test
    @DisplayName("obtenerCredito y obtenerCreditoPorCodigo funcionan")
    void obtenerCredito_funciona() {
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
        Credito c = new Credito();
        c.setCodigo("C-1");
        when(repo.findById("id1")).thenReturn(java.util.Optional.of(c));
        when(repo.findAll()).thenReturn(java.util.List.of(c));
        CreditoService service = buildService(repo, clienteRepo, config, fabrica, interes, score, builder, chain, publisher, template);
        assertTrue(service.obtenerCredito("id1").isPresent());
        assertTrue(service.obtenerCreditoPorCodigo("C-1").isPresent());
    }

    @Test
    @DisplayName("listarCreditosCliente funciona")
    void listarCreditosCliente_funciona() {
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
        when(repo.findByClienteId("cli1")).thenReturn(java.util.List.of(new Credito()));
        CreditoService service = buildService(repo, clienteRepo, config, fabrica, interes, score, builder, chain, publisher, template);
        assertEquals(1, service.listarCreditosCliente("cli1").size());
    }

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
        CreditoService service = buildService(repo, clienteRepo, config, fabrica, interes, score, builder, chain, publisher, template);
        Credito credito = new Credito();
        credito.setSaldo(1234);
        when(repo.findById("id1")).thenReturn(java.util.Optional.of(credito));
        double saldo = service.consultarSaldo("id1");
        assertEquals(1234, saldo);
    }

    @Test
    @DisplayName("consultarSaldo retorna 0.0 si no existe")
    void consultarSaldo_retornaCeroSiNoExiste() {
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
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        CreditoService service = buildService(repo, clienteRepo, config, fabrica, interes, score, builder, chain, publisher, template);
        double saldo = service.consultarSaldo("nope");
        assertEquals(0.0, saldo);
    }
}
