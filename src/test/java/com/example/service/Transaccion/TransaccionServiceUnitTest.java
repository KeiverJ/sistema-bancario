package com.example.service.transaccion;

import com.example.model.transacccion.Transaccion;
import com.example.service.cuenta.CuentaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para TransaccionService (con mocks).
 */
class TransaccionServiceUnitTest {
    @Test
    @DisplayName("registrarTransaccion lanza excepción si monto <= 0")
    void registrarTransaccion_montoInvalido() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(config.getLimiteDiario()).thenReturn(1000.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion t = new Transaccion();
        t.setDescripcion("negativo");
        t.setMonto(0.0);
        assertThrows(IllegalArgumentException.class, () -> service.registrarTransaccion(t));
    }

    @Test
    @DisplayName("registrarTransaccion llama a repo y retorna transacción")
    void registrarTransaccion_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> i.getArgument(0));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion t = new Transaccion();
        t.setDescripcion("unit");
        t.setMonto(100.0);
        Transaccion guardada = service.registrarTransaccion(t);
        assertEquals("unit", guardada.getDescripcion());
    }

    @Test
    @DisplayName("procesarTransaccion lanza excepción si no existe")
    void procesarTransaccion_noExiste() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.findById(anyString())).thenReturn(java.util.Optional.empty());
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertThrows(IllegalArgumentException.class, () -> service.procesarTransaccion("nope"));
    }

    @Test
    @DisplayName("procesarTransaccion retorna tx si no está pendiente")
    void procesarTransaccion_noPendiente() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        Transaccion tx = new Transaccion();
        tx.setEstado(Transaccion.EstadoTransaccion.EXITOSA);
        when(repo.findById("id1")).thenReturn(java.util.Optional.of(tx));
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertSame(tx, service.procesarTransaccion("id1"));
    }

    @Test
    @DisplayName("registrarTransaccion lanza excepción si monto excede límite diario")
    void registrarTransaccion_montoExcedeLimite() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(config.getLimiteDiario()).thenReturn(100.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion t = new Transaccion();
        t.setDescripcion("excede");
        t.setMonto(200.0);
        assertThrows(IllegalArgumentException.class, () -> service.registrarTransaccion(t));
    }

    @Test
    @DisplayName("crearTransferencia llama a factory y registra")
    void crearTransferencia_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> i.getArgument(0));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion tx = service.crearTransferencia("orig", "dest", 100, "desc");
        assertNotNull(tx);
        assertEquals("desc", tx.getDescripcion());
    }

    @Test
    @DisplayName("crearPagoServicio llama a factory y registra")
    void crearPagoServicio_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> i.getArgument(0));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion tx = service.crearPagoServicio("orig", "svc", 100, "ref");
        assertNotNull(tx);
        assertEquals(100, tx.getMonto());
        assertEquals("orig", tx.getCuentaOrigenId());
    }

    @Test
    @DisplayName("crearRetiro llama a factory y registra")
    void crearRetiro_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> i.getArgument(0));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion tx = service.crearRetiro("orig", 100, "cajero");
        assertNotNull(tx);
        assertEquals(100, tx.getMonto());
        assertEquals("orig", tx.getCuentaOrigenId());
    }

    @Test
    @DisplayName("crearDeposito llama a factory y registra")
    void crearDeposito_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> i.getArgument(0));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion tx = service.crearDeposito("dest", 100, "desc");
        assertNotNull(tx);
        assertEquals("desc", tx.getDescripcion());
    }

    @Test
    @DisplayName("procesarTransaccionPorCodigo procesa correctamente")
    void procesarTransaccionPorCodigo_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        Transaccion tx = new Transaccion();
    tx.setId("tid");
    tx.setCodigo("TRX-00001");
    tx.setEstado(Transaccion.EstadoTransaccion.PENDIENTE);
    tx.setTipo(Transaccion.TipoTransaccion.DEPOSITO);
        when(repo.findAll()).thenReturn(java.util.List.of(tx));
        when(repo.findById("tid")).thenReturn(java.util.Optional.of(tx));
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> i.getArgument(0));
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertNotNull(service.procesarTransaccionPorCodigo("TRX-00001"));
    }

    @Test
    @DisplayName("procesarTransaccionPorCodigo lanza excepción si no existe")
    void procesarTransaccionPorCodigo_noExiste() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.findAll()).thenReturn(java.util.List.of());
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertThrows(IllegalArgumentException.class, () -> service.procesarTransaccionPorCodigo("NOPE"));
    }

    @Test
    @DisplayName("registrarYProcesar ejecuta ambos pasos")
    void registrarYProcesar_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> {
            Transaccion t = i.getArgument(0);
            t.setId("tid");
            t.setEstado(Transaccion.EstadoTransaccion.PENDIENTE);
            return t;
        });
        when(repo.findById("tid")).thenReturn(java.util.Optional.of(new Transaccion()));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion t = new Transaccion();
        t.setDescripcion("desc");
        t.setMonto(100.0);
        assertNotNull(service.registrarYProcesar(t));
    }

    @Test
    @DisplayName("obtenerPorCodigo retorna Optional si existe")
    void obtenerPorCodigo_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        Transaccion tx = new Transaccion();
        tx.setCodigo("TRX-00001");
        when(repo.findAll()).thenReturn(java.util.List.of(tx));
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertTrue(service.obtenerPorCodigo("TRX-00001").isPresent());
    }

    @Test
    @DisplayName("obtenerPorCodigo retorna Optional.empty si no existe")
    void obtenerPorCodigo_noExiste() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.findAll()).thenReturn(java.util.List.of());
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertTrue(service.obtenerPorCodigo("NOPE").isEmpty());
    }

    @Test
    @DisplayName("listarTransaccionesCuenta retorna lista")
    void listarTransaccionesCuenta_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.findByCuentaId("cid")).thenReturn(java.util.List.of(new Transaccion()));
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertFalse(service.listarTransaccionesCuenta("cid").isEmpty());
    }

    @Test
    @DisplayName("listarTodasTransacciones retorna lista")
    void listarTodasTransacciones_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.findAll()).thenReturn(java.util.List.of(new Transaccion()));
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertFalse(service.listarTodasTransacciones().isEmpty());
    }

    @Test
    @DisplayName("listarPorRangoFechas retorna lista")
    void listarPorRangoFechas_ok() {
        var repo = mock(com.example.repository.transaccion.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.findByFechaRange(any(), any())).thenReturn(java.util.List.of(new Transaccion()));
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertFalse(service.listarPorRangoFechas(java.time.LocalDateTime.now(), java.time.LocalDateTime.now()).isEmpty());
    }
}
