package com.example.template.core;

import com.example.adapter.score.ScoreProviderRegistry;
import com.example.builder.credito.CreditoBuilderRegistry;
import com.example.chain.core.ApprovalChainBuilder;
import com.example.chain.core.ApprovalContext;
import com.example.config.BankConfig;
import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.model.score.Score;
import com.example.observer.core.DomainEventPublisher;
import com.example.observer.eventos.CreditoEstadoCambiadoEvent;
import com.example.repository.cliente.ClienteRepository;
import com.example.repository.credito.CreditoRepository;
import com.example.strategy.core.InteresStrategyRegistry;

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

    public final Credito solicitar(Cliente cliente, Credito.TipoCredito tipo, double monto, int plazo) {
        preEvaluacion(cliente, tipo, monto, plazo);

        Credito credito = construirCredito(cliente, tipo, monto, plazo);

        configurarTasaYExtras(credito, cliente);

        credito = creditoRepository.save(credito);

        ApprovalContext context = evaluarAprobacion(credito, cliente);

        if (context.isAprobado()) {
            credito.aprobar();
        } else {
            credito.rechazar();
            throw new IllegalStateException("Crédito rechazado: " + context.getMotivoRechazo());
        }

        credito = persistir(credito, cliente);

        notificar(credito, cliente);

        return credito;
    }

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