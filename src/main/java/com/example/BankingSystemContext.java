package com.example;

import com.example.config.BankConfig;
import com.example.repository.cliente.ClienteRepository;
import com.example.repository.cuenta.CuentaRepository;
import com.example.repository.credito.CreditoRepository;
import com.example.repository.transaccion.TransaccionRepository;
import com.example.factory.common.FabricaProductosProvider;
import com.example.strategy.core.InteresStrategyRegistry;
import com.example.builder.credito.CreditoBuilderRegistry;
import com.example.chain.core.ApprovalChainBuilder;
import com.example.adapter.score.ScoreProviderRegistry;
import com.example.adapter.buro.BuroFinancieroAdapter;
import com.example.adapter.legacy.LegacyRiskApiAdapter;
import com.example.observer.core.DomainEventPublisher;
import com.example.service.cliente.ClienteService;
import com.example.service.cuenta.CuentaService;
import com.example.service.credito.CreditoService;
import com.example.service.transaccion.TransaccionService;

public class BankingSystemContext {
    public final ClienteService clienteService;
    public final CuentaService cuentaService;
    public final CreditoService creditoService;
    public final TransaccionService transaccionService;
    public final com.example.controller.ClienteController clienteController;
    public final com.example.controller.CuentaController cuentaController;
    public final com.example.controller.CreditoController creditoController;
    public final com.example.controller.TransaccionController transaccionController;

    public BankingSystemContext() {
        // Configuración y repositorios
        BankConfig bankConfig = new BankConfig();
        ClienteRepository clienteRepository = new ClienteRepository();
        CuentaRepository cuentaRepository = new CuentaRepository();
        CreditoRepository creditoRepository = new CreditoRepository();
        TransaccionRepository transaccionRepository = new TransaccionRepository();

        // Factories y providers
        FabricaProductosProvider fabricaProductosProvider = new FabricaProductosProvider();
        InteresStrategyRegistry interesStrategyRegistry = new InteresStrategyRegistry();
        CreditoBuilderRegistry creditoBuilderRegistry = new CreditoBuilderRegistry();
        ApprovalChainBuilder approvalChainBuilder = new ApprovalChainBuilder();
        ScoreProviderRegistry scoreProviderRegistry = new ScoreProviderRegistry(
                new BuroFinancieroAdapter(clienteRepository),
                new LegacyRiskApiAdapter());

        // Observers y publisher
        DomainEventPublisher publisher = new DomainEventPublisher(java.util.List.of(
                new com.example.observer.impl.LoggingObserver(),
                new com.example.observer.impl.FraudeObserver(),
                new com.example.observer.impl.NotificacionObserver()));

        // Templates
        com.example.template.impl.SolicitudCreditoDefault solicitudCreditoTemplate = new com.example.template.impl.SolicitudCreditoDefault(
                creditoBuilderRegistry,
                interesStrategyRegistry,
                approvalChainBuilder,
                creditoRepository,
                clienteRepository,
                publisher,
                scoreProviderRegistry,
                bankConfig);

        // Servicios
        clienteService = new ClienteService(clienteRepository, bankConfig);
        cuentaService = new CuentaService(cuentaRepository, clienteRepository, bankConfig,
                fabricaProductosProvider, publisher);
        creditoService = new CreditoService(creditoRepository, clienteRepository, bankConfig,
                fabricaProductosProvider, interesStrategyRegistry, scoreProviderRegistry, creditoBuilderRegistry,
                approvalChainBuilder, publisher, solicitudCreditoTemplate);
        transaccionService = new TransaccionService(transaccionRepository, bankConfig, publisher,
                cuentaService);

        // Controladores
        clienteController = new com.example.controller.ClienteController(clienteService);
        cuentaController = new com.example.controller.CuentaController(cuentaService, clienteService, clienteRepository);
        creditoController = new com.example.controller.CreditoController(creditoService, clienteService);
        transaccionController = new com.example.controller.TransaccionController(transaccionService, cuentaService);
    }
}