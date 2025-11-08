package util;

import com.example.adapter.BuroFinancieroAdapter;
import com.example.adapter.LegacyRiskApiAdapter;
import com.example.adapter.ScoreProviderRegistry;
import com.example.builder.CreditoBuilderRegistry;
import com.example.chain.ApprovalChainBuilder;
import com.example.config.BankConfig;
import com.example.observer.DomainEventPublisher;
import com.example.observer.FraudeObserver;
import com.example.observer.LoggingObserver;
import com.example.observer.NotificacionObserver;
import com.example.repository.ClienteRepository;
import com.example.repository.CreditoRepository;
import com.example.repository.CuentaRepository;
import com.example.repository.TransaccionRepository;
import com.example.service.ClienteService;
import com.example.service.CreditoService;
import com.example.service.CuentaService;
import com.example.service.TransaccionService;
import com.example.strategy.InteresStrategyRegistry;
import com.example.template.impl.SolicitudCreditoDefault;
import com.example.factory.FabricaProductosProvider;

import java.util.List;

/**
 * Construye el grafo de dependencias reales (Java puro) para pruebas de integración/E2E.
 * Úsalo en tests para obtener servicios y repositorios configurados.
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
            creditoRepository, clienteRepository, bankConfig, fabricaProductosProvider,
            interesStrategyRegistry, scoreProviderRegistry, creditoBuilderRegistry,
            approvalChainBuilder, publisher, solicitudCreditoTemplate);
    public final TransaccionService transaccionService = new TransaccionService(
            transaccionRepository, bankConfig, publisher, cuentaService);

    public static TestAppContext build() {
        return new TestAppContext();
    }
}
