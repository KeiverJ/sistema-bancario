package com.example.service;

import com.example.model.Cuenta;
import com.example.model.Cliente;
import com.example.repository.CuentaRepository;
import com.example.repository.ClienteRepository;
import com.example.config.BankConfig;
import com.example.factory.FabricaProductosProvider;
import com.example.factory.ProductoBancarioFactory;
import org.springframework.stereotype.Service;
import com.example.observer.DomainEventPublisher;
import com.example.observer.CuentaSaldoActualizadoEvent;

import java.util.List;
import java.util.Optional;

/**
 * Service para operaciones de Cuenta.
 * 
 * @Service hace que Spring Boot cree UNA sola instancia (Singleton).
 */
@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final BankConfig bankConfig;
    private final FabricaProductosProvider fabricaProductosProvider;
    private final DomainEventPublisher publisher;

    // Constructor Injection
    public CuentaService(CuentaRepository cuentaRepository,
            ClienteRepository clienteRepository,
            BankConfig bankConfig,
            FabricaProductosProvider fabricaProductosProvider,
            DomainEventPublisher publisher) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.bankConfig = bankConfig;
        this.fabricaProductosProvider = fabricaProductosProvider;
        this.publisher = publisher;
    }

    public Cuenta abrirCuenta(String clienteId, Cuenta.TipoCuenta tipoCuenta, double saldoInicial) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        double saldoMinimo = bankConfig.getSaldoMinimo(tipoCuenta.name());
        if (saldoInicial < saldoMinimo) {
            throw new IllegalArgumentException(
                    String.format("El saldo inicial debe ser al menos %.2f", saldoMinimo));
        }

        // Abstract Factory según tipo de cliente
        ProductoBancarioFactory factory = fabricaProductosProvider.getFactory(cliente.getTipoCliente());
        Cuenta cuenta = factory.crearCuenta(clienteId, tipoCuenta, saldoInicial);
        cuenta.setCuotaManejo(bankConfig.getCuotaManejo(tipoCuenta.name()));
        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);

        cuenta = cuentaRepository.save(cuenta);

        cliente.agregarCuenta(cuenta.getId());
        clienteRepository.save(cliente);

        return cuenta;
    }

    public boolean depositar(String cuentaId, double monto) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        if (cuenta.depositar(monto)) {
            cuentaRepository.save(cuenta);
            publisher.publish(new CuentaSaldoActualizadoEvent(cuenta));
            return true;
        }
        return false;
    }

    public boolean retirar(String cuentaId, double monto) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        if (cuenta.retirar(monto)) {
            cuentaRepository.save(cuenta);
            publisher.publish(new CuentaSaldoActualizadoEvent(cuenta));
            return true;
        }
        return false;
    }

    public boolean transferir(String cuentaOrigenId, String cuentaDestinoId, double monto) {
        Cuenta origen = cuentaRepository.findById(cuentaOrigenId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta origen no encontrada"));
        Cuenta destino = cuentaRepository.findById(cuentaDestinoId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta destino no encontrada"));
        if (monto > bankConfig.getLimiteDiario()) {
            throw new IllegalArgumentException("El monto excede el límite diario");
        }
        if (!origen.retirar(monto))
            return false;
        if (!destino.depositar(monto)) {
            origen.depositar(monto);
            return false;
        }
        cuentaRepository.save(origen);
        cuentaRepository.save(destino);
        publisher.publish(new CuentaSaldoActualizadoEvent(origen));
        publisher.publish(new CuentaSaldoActualizadoEvent(destino));
        return true;
    }

    public Optional<Cuenta> obtenerCuenta(String id) {
        return cuentaRepository.findById(id);
    }

    public Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    public List<Cuenta> listarCuentasCliente(String clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }

    public double consultarSaldo(String cuentaId) {
        return cuentaRepository.findById(cuentaId)
                .map(Cuenta::getSaldo)
                .orElse(0.0);
    }

    public boolean bloquearCuenta(String cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        cuenta.setEstado(Cuenta.EstadoCuenta.BLOQUEADA);
        cuentaRepository.save(cuenta);
        return true;
    }

    public boolean activarCuenta(String cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        cuentaRepository.save(cuenta);
        return true;
    }

    public List<Cuenta> listarTodasCuentas() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> obtenerCuentaPorCodigo(String codigo) {
        return cuentaRepository.findAll().stream()
                .filter(c -> codigo.equals(c.getCodigo()))
                .findFirst();
    }
}