package com.example.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit (smoke): verificar que las clases de plantilla existen en el classpath.
 */
class TemplateUnitTest {

    @Test
    @DisplayName("Existe clase SolicitudCreditoDefault en classpath")
    void existeSolicitudCreditoDefault() throws Exception {
        Class<?> cls = Class.forName("com.example.template.impl.SolicitudCreditoDefault");
        assertNotNull(cls);
    }

    @Test
    @DisplayName("SolicitudCreditoDefault: flujo exitoso de solicitud")
    void flujoExitosoSolicitud() {
        var builderRegistry = org.mockito.Mockito.mock(com.example.builder.CreditoBuilderRegistry.class);
        var builder = org.mockito.Mockito.mock(com.example.builder.CreditoBuilder.class);
        org.mockito.Mockito.when(builderRegistry.get(org.mockito.Mockito.any())).thenReturn(builder);
        org.mockito.Mockito.when(builder.desdeBase(org.mockito.Mockito.any())).thenReturn(builder);
                org.mockito.Mockito.when(builder.build()).thenAnswer(inv -> {
                        com.example.model.Credito c = new com.example.model.Credito();
                        c.setTipoCredito(com.example.model.Credito.TipoCredito.LIBRE_INVERSION);
                        c.setMonto(10000);
                        c.setPlazoMeses(12);
                        return c;
                });
        var strategyRegistry = org.mockito.Mockito.mock(com.example.strategy.InteresStrategyRegistry.class);
        org.mockito.Mockito.when(strategyRegistry.tasaPara(org.mockito.Mockito.any(), org.mockito.Mockito.any(),
                org.mockito.Mockito.any())).thenReturn(0.1);
        var chainBuilder = org.mockito.Mockito.mock(com.example.chain.ApprovalChainBuilder.class);
        var chain = org.mockito.Mockito.mock(com.example.chain.ApprovalHandler.class);
        org.mockito.Mockito.when(chainBuilder.build(org.mockito.Mockito.any())).thenReturn(chain);
                org.mockito.Mockito.doAnswer(inv -> {
                        ((com.example.chain.ApprovalContext) inv.getArgument(0)).aprobar();
                        return null;
                }).when(chain).handle(org.mockito.Mockito.any());
        var creditoRepo = org.mockito.Mockito.mock(com.example.repository.CreditoRepository.class);
        org.mockito.Mockito.when(creditoRepo.save(org.mockito.Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
        var clienteRepo = org.mockito.Mockito.mock(com.example.repository.ClienteRepository.class);
        org.mockito.Mockito.when(clienteRepo.save(org.mockito.Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
        var publisher = org.mockito.Mockito.mock(com.example.observer.DomainEventPublisher.class);
        var scoreRegistry = org.mockito.Mockito.mock(com.example.adapter.ScoreProviderRegistry.class);
        org.mockito.Mockito.when(scoreRegistry.obtenerScoreParaCliente(org.mockito.Mockito.any()))
                .thenReturn(new com.example.model.Score());
        var bankConfig = org.mockito.Mockito.mock(com.example.config.BankConfig.class);

        var template = new com.example.template.impl.SolicitudCreditoDefault(
                builderRegistry, strategyRegistry, chainBuilder, creditoRepo, clienteRepo, publisher, scoreRegistry,
                bankConfig);
        var cliente = org.mockito.Mockito.mock(com.example.model.Cliente.class);
        org.mockito.Mockito.when(cliente.getId()).thenReturn("cli1");
        org.mockito.Mockito.when(cliente.getTipoCliente())
                .thenReturn(com.example.model.Cliente.TipoCliente.PERSONA_NATURAL);
        var credito = template.solicitar(cliente, com.example.model.Credito.TipoCredito.LIBRE_INVERSION, 10000, 12);
        assertNotNull(credito);
        assertEquals(10000, credito.getMonto());
        assertEquals(12, credito.getPlazoMeses());
    }

    @Test
    @DisplayName("SolicitudCreditoDefault: lanza excepción si monto <= 0")
    void solicitudMontoInvalido() {
        var template = org.mockito.Mockito.mock(com.example.template.impl.SolicitudCreditoDefault.class,
                org.mockito.Mockito.CALLS_REAL_METHODS);
        var cliente = org.mockito.Mockito.mock(com.example.model.Cliente.class);
        assertThrows(IllegalArgumentException.class,
                () -> template.solicitar(cliente, com.example.model.Credito.TipoCredito.LIBRE_INVERSION, 0, 12));
    }

    @Test
    @DisplayName("SolicitudCreditoDefault: lanza excepción si plazo <= 0")
    void solicitudPlazoInvalido() {
        var template = org.mockito.Mockito.mock(com.example.template.impl.SolicitudCreditoDefault.class,
                org.mockito.Mockito.CALLS_REAL_METHODS);
        var cliente = org.mockito.Mockito.mock(com.example.model.Cliente.class);
        assertThrows(IllegalArgumentException.class,
                () -> template.solicitar(cliente, com.example.model.Credito.TipoCredito.LIBRE_INVERSION, 10000, 0));
    }

    @Test
    @DisplayName("SolicitudCreditoDefault: lanza excepción si crédito es rechazado")
    void solicitudRechazada() {
        var builderRegistry = org.mockito.Mockito.mock(com.example.builder.CreditoBuilderRegistry.class);
        var builder = org.mockito.Mockito.mock(com.example.builder.CreditoBuilder.class);
        org.mockito.Mockito.when(builderRegistry.get(org.mockito.Mockito.any())).thenReturn(builder);
        org.mockito.Mockito.when(builder.desdeBase(org.mockito.Mockito.any())).thenReturn(builder);
                org.mockito.Mockito.when(builder.build()).thenAnswer(inv -> {
                        com.example.model.Credito c = new com.example.model.Credito();
                        c.setTipoCredito(com.example.model.Credito.TipoCredito.LIBRE_INVERSION);
                        c.setMonto(10000);
                        c.setPlazoMeses(12);
                        return c;
                });
        var strategyRegistry = org.mockito.Mockito.mock(com.example.strategy.InteresStrategyRegistry.class);
        org.mockito.Mockito.when(strategyRegistry.tasaPara(org.mockito.Mockito.any(), org.mockito.Mockito.any(),
                org.mockito.Mockito.any())).thenReturn(0.1);
        var chainBuilder = org.mockito.Mockito.mock(com.example.chain.ApprovalChainBuilder.class);
        var chain = org.mockito.Mockito.mock(com.example.chain.ApprovalHandler.class);
        org.mockito.Mockito.when(chainBuilder.build(org.mockito.Mockito.any())).thenReturn(chain);
                org.mockito.Mockito.doAnswer(inv -> {
                        ((com.example.chain.ApprovalContext) inv.getArgument(0)).rechazar("NO");
                        return null;
                }).when(chain).handle(org.mockito.Mockito.any());
        var creditoRepo = org.mockito.Mockito.mock(com.example.repository.CreditoRepository.class);
        org.mockito.Mockito.when(creditoRepo.save(org.mockito.Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
        var clienteRepo = org.mockito.Mockito.mock(com.example.repository.ClienteRepository.class);
        org.mockito.Mockito.when(clienteRepo.save(org.mockito.Mockito.any())).thenAnswer(inv -> inv.getArgument(0));
        var publisher = org.mockito.Mockito.mock(com.example.observer.DomainEventPublisher.class);
        var scoreRegistry = org.mockito.Mockito.mock(com.example.adapter.ScoreProviderRegistry.class);
        org.mockito.Mockito.when(scoreRegistry.obtenerScoreParaCliente(org.mockito.Mockito.any()))
                .thenReturn(new com.example.model.Score());
        var bankConfig = org.mockito.Mockito.mock(com.example.config.BankConfig.class);

        var template = new com.example.template.impl.SolicitudCreditoDefault(
                builderRegistry, strategyRegistry, chainBuilder, creditoRepo, clienteRepo, publisher, scoreRegistry,
                bankConfig);
        var cliente = org.mockito.Mockito.mock(com.example.model.Cliente.class);
        org.mockito.Mockito.when(cliente.getId()).thenReturn("cli1");
        org.mockito.Mockito.when(cliente.getTipoCliente())
                .thenReturn(com.example.model.Cliente.TipoCliente.PERSONA_NATURAL);
        assertThrows(IllegalStateException.class,
                () -> template.solicitar(cliente, com.example.model.Credito.TipoCredito.LIBRE_INVERSION, 10000, 12));
    }
}
