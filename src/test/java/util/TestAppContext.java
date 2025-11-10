package util;

import com.example.adapter.buro.BuroFinancieroAdapter;
import com.example.adapter.legacy.LegacyRiskApiAdapter;
import com.example.adapter.score.ScoreProviderRegistry;
import com.example.builder.credito.CreditoBuilderRegistry;
import com.example.chain.core.ApprovalChainBuilder;
import com.example.config.BankConfig;
import com.example.factory.common.FabricaProductosProvider;
import com.example.observer.core.DomainEventPublisher;
import com.example.observer.impl.FraudeObserver;
import com.example.observer.impl.LoggingObserver;
import com.example.observer.impl.NotificacionObserver;
import com.example.repository.cliente.ClienteRepository;
import com.example.repository.credito.CreditoRepository;
import com.example.repository.cuenta.CuentaRepository;
import com.example.repository.transaccion.TransaccionRepository;
import com.example.service.cliente.ClienteService;
import com.example.service.credito.CreditoService;
import com.example.service.cuenta.CuentaService;
import com.example.service.transaccion.TransaccionService;
import com.example.strategy.core.InteresStrategyRegistry;
import com.example.template.impl.SolicitudCreditoDefault;

import java.util.List;

/**
 * Construye el grafo de dependencias reales (Java puro) para pruebas de integración/E2E.
 */
public class TestAppContext {

    // Config
    public final BankConfig bankConfig = new BankConfig();

    // Repositorios en memoria
    public final ClienteRepository clienteRepository = new ClienteRepository();
    public final CuentaRepository cuentaRepository = new CuentaRepository();
    public final CreditoRepository creditoRepository = new CreditoRepository();
    public final TransaccionRepository transaccionRepository = new TransaccionRepository();

    // Componentes auxiliares
    public final FabricaProductosProvider fabricaProductosProvider = new FabricaProductosProvider();
    public final InteresStrategyRegistry interesStrategyRegistry = new InteresStrategyRegistry();
    public final CreditoBuilderRegistry creditoBuilderRegistry = new CreditoBuilderRegistry();
    public final ApprovalChainBuilder approvalChainBuilder = new ApprovalChainBuilder();
    public final ScoreProviderRegistry scoreProviderRegistry = new ScoreProviderRegistry(
            new BuroFinancieroAdapter(clienteRepository),
            new LegacyRiskApiAdapter());

    // Observers y publisher
    public final DomainEventPublisher publisher = new DomainEventPublisher(List.of(
            new LoggingObserver(),
            new FraudeObserver(),
            new NotificacionObserver()
    ));

    // Templates
    public final SolicitudCreditoDefault solicitudCreditoTemplate = new SolicitudCreditoDefault(
            creditoBuilderRegistry,
            interesStrategyRegistry,
            approvalChainBuilder,
            creditoRepository,
            clienteRepository,
            publisher,
            scoreProviderRegistry,
            bankConfig
    );

    // Servicios
    public final ClienteService clienteService = new ClienteService(clienteRepository, bankConfig);
    public final CuentaService cuentaService = new CuentaService(
            cuentaRepository, clienteRepository, bankConfig, fabricaProductosProvider, publisher);
    public final CreditoService creditoService = new CreditoService(
            creditoRepository, clienteRepository, publisher, solicitudCreditoTemplate);
    public final TransaccionService transaccionService = new TransaccionService(
            transaccionRepository, bankConfig, publisher, cuentaService);

    public static TestAppContext build() {
        return new TestAppContext();
    }
}
