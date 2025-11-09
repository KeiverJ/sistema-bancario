package com.example.template.impl;

import com.example.config.BankConfig;
import com.example.factory.common.FabricaProductosProvider;
import com.example.factory.common.ProductoBancarioFactory;
import com.example.model.cliente.Cliente;
import com.example.model.cuenta.Cuenta;
import com.example.observer.core.DomainEventPublisher;
import com.example.repository.cliente.ClienteRepository;
import com.example.repository.cuenta.CuentaRepository;
import com.example.template.core.AperturaCuentaTemplate;

public class AperturaCuentaDefault extends AperturaCuentaTemplate {

    private final BankConfig bankConfig;
    private final FabricaProductosProvider fabrica;
    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public AperturaCuentaDefault(BankConfig bankConfig,
            FabricaProductosProvider fabrica,
            CuentaRepository cuentaRepository,
            ClienteRepository clienteRepository) {
        this.bankConfig = bankConfig;
        this.fabrica = fabrica;
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;

    }

    @Override
    protected void validarReglas(Cliente cliente, Cuenta.TipoCuenta tipoCuenta, double saldoInicial) {
        double min = bankConfig.getSaldoMinimo(tipoCuenta.name());
        if (saldoInicial < min)
            throw new IllegalArgumentException("El saldo inicial debe ser al menos " + min);
    }

    @Override
    protected Cuenta construirCuenta(Cliente cliente, Cuenta.TipoCuenta tipoCuenta, double saldoInicial) {
        ProductoBancarioFactory factory = fabrica.getFactory(cliente.getTipoCliente());
        Cuenta c = factory.crearCuenta(cliente.getId(), tipoCuenta, saldoInicial);
        c.setCuotaManejo(bankConfig.getCuotaManejo(tipoCuenta.name()));
        c.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        return c;
    }

    @Override
    protected void persist(Cuenta cuenta, Cliente cliente) {
        Cuenta saved = cuentaRepository.save(cuenta);
        cliente.agregarCuenta(saved.getId());
        clienteRepository.save(cliente);
    }
}