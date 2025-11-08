package com.example.service;

import com.example.model.Credito;
import com.example.model.Cliente;
import com.example.repository.CreditoRepository;
import com.example.repository.ClienteRepository;
import com.example.config.BankConfig;
import com.example.factory.FabricaProductosProvider;
import com.example.factory.ProductoBancarioFactory;
import com.example.strategy.InteresStrategyRegistry;
import com.example.adapter.ScoreProviderRegistry;
import com.example.model.Score;
import com.example.builder.CreditoBuilderRegistry;
import com.example.builder.CreditoBuilder;
import com.example.chain.ApprovalChainBuilder;
import com.example.chain.ApprovalContext;
import com.example.chain.ApprovalHandler;
import com.example.observer.DomainEventPublisher;
import com.example.observer.CreditoEstadoCambiadoEvent;
import com.example.template.SolicitudCreditoTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreditoService {

    private final CreditoRepository creditoRepository;
    private final ClienteRepository clienteRepository;
    private final BankConfig bankConfig;
    private final FabricaProductosProvider fabricaProductosProvider;
    private final InteresStrategyRegistry interesStrategyRegistry;
    private final ScoreProviderRegistry scoreProviderRegistry;
    private final CreditoBuilderRegistry creditoBuilderRegistry;
    private final ApprovalChainBuilder approvalChainBuilder;
    private final DomainEventPublisher publisher;
    private final SolicitudCreditoTemplate solicitudCreditoTemplate;

    public CreditoService(CreditoRepository creditoRepository,
            ClienteRepository clienteRepository,
            BankConfig bankConfig,
            FabricaProductosProvider fabricaProductosProvider,
            InteresStrategyRegistry interesStrategyRegistry,
            ScoreProviderRegistry scoreProviderRegistry,
            CreditoBuilderRegistry creditoBuilderRegistry,
            ApprovalChainBuilder approvalChainBuilder,
            DomainEventPublisher publisher,
            SolicitudCreditoTemplate solicitudCreditoTemplate) {
        this.creditoRepository = creditoRepository;
        this.clienteRepository = clienteRepository;
        this.bankConfig = bankConfig;
        this.fabricaProductosProvider = fabricaProductosProvider;
        this.interesStrategyRegistry = interesStrategyRegistry;
        this.scoreProviderRegistry = scoreProviderRegistry;
        this.creditoBuilderRegistry = creditoBuilderRegistry;
        this.approvalChainBuilder = approvalChainBuilder;
        this.publisher = publisher;
        this.solicitudCreditoTemplate = solicitudCreditoTemplate;
    }

    public Credito solicitarCredito(String clienteId, Credito.TipoCredito tipoCredito,
            double monto, int plazoMeses) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        return solicitudCreditoTemplate.solicitar(cliente, tipoCredito, monto, plazoMeses);
    }

    public Optional<Credito> obtenerCredito(String id) {
        return creditoRepository.findById(id);
    }

    public List<Credito> listarCreditosCliente(String clienteId) {
        return creditoRepository.findByClienteId(clienteId);
    }

    public Optional<Credito> obtenerCreditoPorCodigo(String codigo) {
        return creditoRepository.findAll().stream()
                .filter(cr -> codigo.equals(cr.getCodigo()))
                .findFirst();
    }

    public List<Credito> listarCreditosClientePorCodigo(String codigoCliente) {
        var clienteOpt = clienteRepository.findAll().stream()
                .filter(c -> codigoCliente.equals(c.getCodigo()))
                .findFirst();
        if (clienteOpt.isEmpty())
            throw new IllegalArgumentException("Cliente no encontrado");
        String clienteId = clienteOpt.get().getId();
        return creditoRepository.findAll().stream()
                .filter(cr -> clienteId.equals(cr.getClienteId()))
                .collect(Collectors.toList());
    }

    public boolean pagarCuota(String creditoId, double montoPago) {
        Credito credito = creditoRepository.findById(creditoId)
                .orElseThrow(() -> new IllegalArgumentException("Crédito no encontrado"));
        boolean ok = credito.pagarCuota(montoPago);
        if (ok) {
            Credito saved = creditoRepository.save(credito);
            publisher.publish(new CreditoEstadoCambiadoEvent(saved));
        }
        return ok;
    }

    public double consultarSaldo(String creditoId) {
        return creditoRepository.findById(creditoId)
                .map(Credito::getSaldo)
                .orElse(0.0);
    }

    public double getTotalEndeudamiento(String clienteId) {
        return creditoRepository.getTotalSaldoByClienteId(clienteId);
    }

    public double calcularInteresMensual(String creditoId) {
        Credito credito = creditoRepository.findById(creditoId)
                .orElseThrow(() -> new IllegalArgumentException("Crédito no encontrado"));
        return credito.getSaldo() * (credito.getTasaInteres() / 12.0) / 100.0;
    }

    public List<Credito> listarTodosCreditos() {
        return creditoRepository.findAll();
    }

    /**
     * Guarda un crédito y publica el evento de cambio de estado
     */
    public Credito guardar(Credito credito) {
        Credito saved = creditoRepository.save(credito);
        publisher.publish(new CreditoEstadoCambiadoEvent(saved));
        return saved;
    }
}