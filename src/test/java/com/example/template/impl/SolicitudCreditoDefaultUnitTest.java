package com.example.template.impl;

import com.example.builder.CreditoBuilder;
import com.example.builder.CreditoBuilderRegistry;
import com.example.chain.ApprovalChainBuilder;
import com.example.chain.ApprovalHandler;
import com.example.config.BankConfig;
import com.example.model.Cliente;
import com.example.model.Credito;
import com.example.observer.DomainEventPublisher;
import com.example.repository.ClienteRepository;
import com.example.repository.CreditoRepository;
import com.example.strategy.InteresStrategyRegistry;
import com.example.adapter.ScoreProviderRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitudCreditoDefaultUnitTest {
    @Test
    @DisplayName("SolicitudCreditoDefault: flujo exitoso de solicitud")
    void flujoExitoso() {
        CreditoBuilderRegistry builderRegistry = mock(CreditoBuilderRegistry.class);
        CreditoBuilder builder = mock(CreditoBuilder.class);
        when(builderRegistry.get(any())).thenReturn(builder);
        when(builder.desdeBase(any())).thenReturn(builder);
        when(builder.build()).thenAnswer(inv -> {
            Credito c = new Credito();
            c.setTipoCredito(Credito.TipoCredito.LIBRE_INVERSION);
            c.setMonto(10000);
            c.setPlazoMeses(12);
            return c;
        });
        InteresStrategyRegistry strategyRegistry = mock(InteresStrategyRegistry.class);
        when(strategyRegistry.tasaPara(any(), any(), any())).thenReturn(0.1);
        ApprovalChainBuilder chainBuilder = mock(ApprovalChainBuilder.class);
        ApprovalHandler chain = mock(ApprovalHandler.class);
        when(chainBuilder.build(any())).thenReturn(chain);
        doAnswer(inv -> { ((com.example.chain.ApprovalContext)inv.getArgument(0)).aprobar(); return null; }).when(chain).handle(any());
        CreditoRepository creditoRepo = mock(CreditoRepository.class);
        when(creditoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ClienteRepository clienteRepo = mock(ClienteRepository.class);
        when(clienteRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        DomainEventPublisher publisher = mock(DomainEventPublisher.class);
        ScoreProviderRegistry scoreRegistry = mock(ScoreProviderRegistry.class);
        when(scoreRegistry.obtenerScoreParaCliente(any())).thenReturn(new com.example.model.Score());
        BankConfig bankConfig = mock(BankConfig.class);
        SolicitudCreditoDefault tpl = new SolicitudCreditoDefault(builderRegistry, strategyRegistry, chainBuilder, creditoRepo, clienteRepo, publisher, scoreRegistry, bankConfig);
        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn("cli1");
        when(cliente.getTipoCliente()).thenReturn(Cliente.TipoCliente.PERSONA_NATURAL);
        Credito credito = tpl.solicitar(cliente, Credito.TipoCredito.LIBRE_INVERSION, 10000, 12);
        assertNotNull(credito);
        assertEquals(10000, credito.getMonto());
        assertEquals(12, credito.getPlazoMeses());
        assertEquals(Credito.TipoCredito.LIBRE_INVERSION, credito.getTipoCredito());
    }

    @Test
    @DisplayName("SolicitudCreditoDefault: lanza excepción si monto <= 0")
    void montoInvalido() {
        SolicitudCreditoDefault tpl = mock(SolicitudCreditoDefault.class, CALLS_REAL_METHODS);
        Cliente cliente = mock(Cliente.class);
        assertThrows(IllegalArgumentException.class, () -> tpl.solicitar(cliente, Credito.TipoCredito.LIBRE_INVERSION, 0, 12));
    }

    @Test
    @DisplayName("SolicitudCreditoDefault: lanza excepción si plazo <= 0")
    void plazoInvalido() {
        SolicitudCreditoDefault tpl = mock(SolicitudCreditoDefault.class, CALLS_REAL_METHODS);
        Cliente cliente = mock(Cliente.class);
        assertThrows(IllegalArgumentException.class, () -> tpl.solicitar(cliente, Credito.TipoCredito.LIBRE_INVERSION, 10000, 0));
    }

    @Test
    @DisplayName("SolicitudCreditoDefault: lanza excepción si crédito es rechazado")
    void solicitudRechazada() {
        CreditoBuilderRegistry builderRegistry = mock(CreditoBuilderRegistry.class);
        CreditoBuilder builder = mock(CreditoBuilder.class);
        when(builderRegistry.get(any())).thenReturn(builder);
        when(builder.desdeBase(any())).thenReturn(builder);
        when(builder.build()).thenAnswer(inv -> {
            Credito c = new Credito();
            c.setTipoCredito(Credito.TipoCredito.LIBRE_INVERSION);
            c.setMonto(10000);
            c.setPlazoMeses(12);
            return c;
        });
        InteresStrategyRegistry strategyRegistry = mock(InteresStrategyRegistry.class);
        when(strategyRegistry.tasaPara(any(), any(), any())).thenReturn(0.1);
        ApprovalChainBuilder chainBuilder = mock(ApprovalChainBuilder.class);
        ApprovalHandler chain = mock(ApprovalHandler.class);
        when(chainBuilder.build(any())).thenReturn(chain);
        doAnswer(inv -> { ((com.example.chain.ApprovalContext)inv.getArgument(0)).rechazar("NO"); return null; }).when(chain).handle(any());
        CreditoRepository creditoRepo = mock(CreditoRepository.class);
        when(creditoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ClienteRepository clienteRepo = mock(ClienteRepository.class);
        when(clienteRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        DomainEventPublisher publisher = mock(DomainEventPublisher.class);
        ScoreProviderRegistry scoreRegistry = mock(ScoreProviderRegistry.class);
        when(scoreRegistry.obtenerScoreParaCliente(any())).thenReturn(new com.example.model.Score());
        BankConfig bankConfig = mock(BankConfig.class);
        SolicitudCreditoDefault tpl = new SolicitudCreditoDefault(builderRegistry, strategyRegistry, chainBuilder, creditoRepo, clienteRepo, publisher, scoreRegistry, bankConfig);
        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn("cli1");
        when(cliente.getTipoCliente()).thenReturn(Cliente.TipoCliente.PERSONA_NATURAL);
        assertThrows(IllegalStateException.class, () -> tpl.solicitar(cliente, Credito.TipoCredito.LIBRE_INVERSION, 10000, 12));
    }
}
