package com.example.template.impl;

import com.example.adapter.ScoreProviderRegistry;
import com.example.builder.CreditoBuilder;
import com.example.builder.CreditoBuilderRegistry;
import com.example.chain.ApprovalChainBuilder;
import com.example.config.BankConfig;
import com.example.model.Cliente;
import com.example.model.Credito;
import com.example.observer.DomainEventPublisher;
import com.example.repository.ClienteRepository;
import com.example.repository.CreditoRepository;
import com.example.strategy.InteresStrategyRegistry;
import com.example.template.SolicitudCreditoTemplate;
import com.example.util.CreditoCodigoGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SolicitudCreditoDefault extends SolicitudCreditoTemplate {

    public SolicitudCreditoDefault(
            CreditoBuilderRegistry builderRegistry,
            InteresStrategyRegistry strategyRegistry,
            ApprovalChainBuilder chainBuilder,
            CreditoRepository creditoRepository,
            ClienteRepository clienteRepository,
            DomainEventPublisher publisher,
            ScoreProviderRegistry scoreProviderRegistry,
            BankConfig bankConfig) {
        super(builderRegistry, strategyRegistry, chainBuilder, creditoRepository,
                clienteRepository, publisher, scoreProviderRegistry, bankConfig);
    }

    @Override
    protected Credito construirCredito(Cliente cliente, Credito.TipoCredito tipo, double monto, int plazo) {
        // Crear un crédito base
        Credito credito = new Credito();

        // Generar ID único
        credito.setId(UUID.randomUUID().toString());

        // Generar código basado en el tipo de cliente
        credito.setCodigo(CreditoCodigoGenerator.generarCodigo(cliente.getTipoCliente()));

        // Configurar datos básicos
        credito.setClienteId(cliente.getId());
        credito.setTipoCredito(tipo);
        credito.setMonto(monto);
        credito.setPlazoMeses(plazo);
        credito.setSaldo(monto); // El saldo inicial es igual al monto
        credito.setEstadoActual(Credito.EstadoCredito.SOLICITADO); // Estado inicial

        // Usar el builder para configuraciones adicionales específicas del tipo
        CreditoBuilder builder = builderRegistry.get(tipo);
        return builder.desdeBase(credito).build();
    }

    @Override
    protected void configurarTasaYExtras(Credito credito, Cliente cliente) {
        // Calcular tasa usando el registry
        double tasa = strategyRegistry.tasaPara(credito, cliente, bankConfig);
        credito.setTasaInteres(tasa);

        // Configurar extras según tipo
        switch (credito.getTipoCredito()) {
            case HIPOTECARIO:
                credito.setGarantia("HIPOTECA");
                credito.setSeguroVida(true);
                credito.setCostoApertura(credito.getMonto() * 0.01); // 1%
                break;
            case VEHICULO:
                credito.setGarantia("PRENDA");
                credito.setSeguroVida(true);
                credito.setCostoApertura(credito.getMonto() * 0.005); // 0.5%
                break;
            case CONSUMO:
            case LIBRE_INVERSION:
                credito.setGarantia("SIN_GARANTIA");
                credito.setCuotaAdministracion(10000.0);
                break;
        }
    }
}