package com.example.template;

import com.example.adapter.ScoreProviderRegistry;
import com.example.builder.CreditoBuilderRegistry;
import com.example.chain.ApprovalChainBuilder;
import com.example.chain.ApprovalContext;
import com.example.config.BankConfig;
import com.example.model.Cliente;
import com.example.model.Credito;
import com.example.model.Score;
import com.example.observer.CreditoEstadoCambiadoEvent;
import com.example.observer.DomainEventPublisher;
import com.example.repository.ClienteRepository;
import com.example.repository.CreditoRepository;
import com.example.strategy.InteresStrategyRegistry;

public abstract class SolicitudCreditoTemplate {

    protected final CreditoBuilderRegistry builderRegistry;
    protected final InteresStrategyRegistry strategyRegistry;
    protected final ApprovalChainBuilder chainBuilder;
    protected final CreditoRepository creditoRepository;
    protected final ClienteRepository clienteRepository;
    protected final DomainEventPublisher publisher;
    protected final ScoreProviderRegistry scoreProviderRegistry;
    protected final BankConfig bankConfig;

    public SolicitudCreditoTemplate(
            CreditoBuilderRegistry builderRegistry,
            InteresStrategyRegistry strategyRegistry,
            ApprovalChainBuilder chainBuilder,
            CreditoRepository creditoRepository,
            ClienteRepository clienteRepository,
            DomainEventPublisher publisher,
            ScoreProviderRegistry scoreProviderRegistry,
            BankConfig bankConfig) {
        this.builderRegistry = builderRegistry;
        this.strategyRegistry = strategyRegistry;
        this.chainBuilder = chainBuilder;
        this.creditoRepository = creditoRepository;
        this.clienteRepository = clienteRepository;
        this.publisher = publisher;
        this.scoreProviderRegistry = scoreProviderRegistry;
        this.bankConfig = bankConfig;
    }

    // Template Method principal
    public final Credito solicitar(Cliente cliente, Credito.TipoCredito tipo, double monto, int plazo) {
        // 1. Pre-evaluación
        preEvaluacion(cliente, tipo, monto, plazo);

        // 2. Construir crédito
        Credito credito = construirCredito(cliente, tipo, monto, plazo);

        // 3. Configurar tasa y extras
        configurarTasaYExtras(credito, cliente);

        // 4. Persistir en estado SOLICITADO
        credito = creditoRepository.save(credito);

        // 5. Evaluar aprobación
        ApprovalContext context = evaluarAprobacion(credito, cliente);

        // 6. Procesar resultado
        if (context.isAprobado()) {
            credito.aprobar();
        } else {
            credito.rechazar();
            throw new IllegalStateException("Crédito rechazado: " + context.getMotivoRechazo());
        }

        // 7. Persistir cambio de estado
        credito = persistir(credito, cliente);

        // 8. Notificar
        notificar(credito, cliente);

        return credito;
    }

    // Hooks con implementación por defecto
    protected void preEvaluacion(Cliente cliente, Credito.TipoCredito tipo, double monto, int plazo) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (plazo <= 0) {
            throw new IllegalArgumentException("El plazo debe ser positivo");
        }
    }

    protected abstract Credito construirCredito(Cliente cliente, Credito.TipoCredito tipo, double monto, int plazo);

    protected abstract void configurarTasaYExtras(Credito credito, Cliente cliente);

    protected ApprovalContext evaluarAprobacion(Credito credito, Cliente cliente) {
        Score score = scoreProviderRegistry.obtenerScoreParaCliente(cliente);
        ApprovalContext context = new ApprovalContext(cliente, credito, score, bankConfig);
        chainBuilder.build(bankConfig).handle(context);
        return context;
    }

    protected Credito persistir(Credito credito, Cliente cliente) {
        credito = creditoRepository.save(credito);
        cliente.agregarCredito(credito.getId());
        clienteRepository.save(cliente);
        return credito;
    }

    protected void notificar(Credito credito, Cliente cliente) {
        publisher.publish(new CreditoEstadoCambiadoEvent(credito));
    }
}