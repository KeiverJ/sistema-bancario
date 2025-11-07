package com.example.service;

import com.example.config.BankConfig;
import com.example.factory.DepositoFactory;
import com.example.factory.PagoServicioFactory;
import com.example.factory.RetiroFactory;
import com.example.factory.TransferenciaFactory;
import com.example.model.Transaccion;
import com.example.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import com.example.observer.DomainEventPublisher;
import com.example.observer.TransaccionRegistradaEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final BankConfig bankConfig;
    private final DomainEventPublisher publisher;
    private final CuentaService cuentaService;
    private final AtomicInteger contadorTx = new AtomicInteger(1);

    public TransaccionService(TransaccionRepository transaccionRepository, BankConfig bankConfig,
            DomainEventPublisher publisher, CuentaService cuentaService) {
        this.transaccionRepository = transaccionRepository;
        this.bankConfig = bankConfig;
        this.publisher = publisher;
        this.cuentaService = cuentaService;
    }

    public Transaccion registrarTransaccion(Transaccion t) {
        if (t.getMonto() <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        if (t.getMonto() > bankConfig.getLimiteDiario())
            throw new IllegalArgumentException("El monto excede el límite diario");
        t.setId(UUID.randomUUID().toString());
        t.setCodigo("TRX-" + String.format("%05d", contadorTx.getAndIncrement()));
        t.setFecha(LocalDateTime.now());
        t.setEstado(Transaccion.EstadoTransaccion.PENDIENTE);
        Transaccion saved = transaccionRepository.save(t);
        publisher.publish(new TransaccionRegistradaEvent(saved));
        return saved;
    }

    // Procesa una transacción pendiente y la marca EXITOSA/FALLIDA
    public Transaccion procesarTransaccion(String transaccionId) {
        Transaccion tx = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));
        if (tx.getEstado() != Transaccion.EstadoTransaccion.PENDIENTE)
            return tx;

        boolean ok = false;
        switch (tx.getTipo()) {
            case DEPOSITO -> ok = cuentaService.depositar(tx.getCuentaDestinoId(), tx.getMonto());
            case RETIRO -> ok = cuentaService.retirar(tx.getCuentaOrigenId(), tx.getMonto());
            case TRANSFERENCIA ->
                ok = cuentaService.transferir(tx.getCuentaOrigenId(), tx.getCuentaDestinoId(), tx.getMonto());
            case PAGO_SERVICIO -> ok = cuentaService.retirar(tx.getCuentaOrigenId(), tx.getMonto());
        }
        tx.setEstado(ok ? Transaccion.EstadoTransaccion.EXITOSA : Transaccion.EstadoTransaccion.FALLIDA);
        return transaccionRepository.save(tx);
    }

    // Si quieres registrar y procesar en un solo paso
    public Transaccion registrarYProcesar(Transaccion t) {
        Transaccion saved = registrarTransaccion(t);
        return procesarTransaccion(saved.getId());
    }

    public Optional<Transaccion> obtenerTransaccion(String id) {
        return transaccionRepository.findById(id);
    }

    public List<Transaccion> listarTransaccionesCuenta(String cuentaId) {
        return transaccionRepository.findByCuentaId(cuentaId);
    }

    public List<Transaccion> listarTodasTransacciones() {
        return transaccionRepository.findAll();
    }

    public List<Transaccion> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return transaccionRepository.findByFechaRange(inicio, fin);
    }

    // Factory Method helpers (déjalos como registrar o cámbialos a
    // registrarYProcesar)
    public Transaccion crearTransferencia(String origenId, String destinoId, double monto, String descripcion) {
        Transaccion tx = new TransferenciaFactory(origenId, destinoId, monto, descripcion).nueva();
        return registrarTransaccion(tx); // o registrarYProcesar(tx);
    }

    public Transaccion crearPagoServicio(String origenId, String codigoServicio, double monto, String referencia) {
        Transaccion tx = new PagoServicioFactory(origenId, codigoServicio, monto, referencia).nueva();
        return registrarTransaccion(tx); // o registrarYProcesar(tx);
    }

    public Transaccion crearRetiro(String origenId, double monto, String ubicacion) {
        Transaccion tx = new RetiroFactory(origenId, monto, ubicacion).nueva();
        return registrarTransaccion(tx); // o registrarYProcesar(tx);
    }

    public Transaccion crearDeposito(String destinoId, double monto, String descripcion) {
        Transaccion tx = new DepositoFactory(destinoId, monto, descripcion).nueva();
        return registrarTransaccion(tx); // o registrarYProcesar(tx);
    }

    public Optional<Transaccion> obtenerPorCodigo(String codigo) {
        return transaccionRepository.findAll().stream()
                .filter(t -> codigo.equals(t.getCodigo()))
                .findFirst();
    }

    public Transaccion procesarTransaccionPorCodigo(String codigo) {
        Transaccion tx = obtenerPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));
        return procesarTransaccion(tx.getId());
    }
}