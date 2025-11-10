package com.example.service.cuenta;

import com.example.model.cliente.Cliente;
import com.example.model.cuenta.Cuenta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CuentaService (con mocks).
 */
class CuentaServiceUnitTest {
    @Test
    @DisplayName("transferir transfiere correctamente")
    void transferir_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta origen = mock(Cuenta.class);
        Cuenta destino = mock(Cuenta.class);
        when(repo.findById("o")).thenReturn(java.util.Optional.of(origen));
        when(repo.findById("d")).thenReturn(java.util.Optional.of(destino));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        when(origen.retirar(100)).thenReturn(true);
        when(destino.depositar(100)).thenReturn(true);
        when(repo.save(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.transferir("o", "d", 100));
        verify(origen).retirar(100);
        verify(destino).depositar(100);
        verify(publisher, times(2)).publish(any());
    }

    @Test
    @DisplayName("transferir lanza excepción si cuenta origen no existe")
    void transferir_origenNoExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findById("o")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () -> service.transferir("o", "d", 100));
    }

    @Test
    @DisplayName("transferir lanza excepción si cuenta destino no existe")
    void transferir_destinoNoExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta origen = mock(Cuenta.class);
        when(repo.findById("o")).thenReturn(java.util.Optional.of(origen));
        when(repo.findById("d")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () -> service.transferir("o", "d", 100));
    }

    @Test
    @DisplayName("transferir lanza excepción si monto excede límite")
    void transferir_excedeLimite() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta origen = mock(Cuenta.class);
        Cuenta destino = mock(Cuenta.class);
        when(repo.findById("o")).thenReturn(java.util.Optional.of(origen));
        when(repo.findById("d")).thenReturn(java.util.Optional.of(destino));
        when(config.getLimiteDiario()).thenReturn(50.0);
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () -> service.transferir("o", "d", 100));
    }

    @Test
    @DisplayName("transferir retorna false si origen no puede retirar")
    void transferir_origenNoRetira() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta origen = mock(Cuenta.class);
        Cuenta destino = mock(Cuenta.class);
        when(repo.findById("o")).thenReturn(java.util.Optional.of(origen));
        when(repo.findById("d")).thenReturn(java.util.Optional.of(destino));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        when(origen.retirar(100)).thenReturn(false);
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertFalse(service.transferir("o", "d", 100));
    }

    @Test
    @DisplayName("transferir retorna false si destino no puede depositar y revierte")
    void transferir_destinoNoDeposita() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta origen = mock(Cuenta.class);
        Cuenta destino = mock(Cuenta.class);
        when(repo.findById("o")).thenReturn(java.util.Optional.of(origen));
        when(repo.findById("d")).thenReturn(java.util.Optional.of(destino));
        when(config.getLimiteDiario()).thenReturn(1000.0);
        when(origen.retirar(100)).thenReturn(true);
        when(destino.depositar(100)).thenReturn(false);
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertFalse(service.transferir("o", "d", 100));
        verify(origen).depositar(100);
    }

    @Test
    @DisplayName("bloquearCuenta bloquea correctamente")
    void bloquearCuenta_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = new Cuenta();
        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        when(repo.findById("c1")).thenReturn(java.util.Optional.of(cuenta));
        when(repo.save(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.bloquearCuenta("c1"));
        assertEquals(Cuenta.EstadoCuenta.BLOQUEADA, cuenta.getEstado());
    }

    @Test
    @DisplayName("bloquearCuenta lanza excepción si no existe")
    void bloquearCuenta_noExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () -> service.bloquearCuenta("nope"));
    }

    @Test
    @DisplayName("activarCuenta activa correctamente")
    void activarCuenta_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = new Cuenta();
        cuenta.setEstado(Cuenta.EstadoCuenta.BLOQUEADA);
        when(repo.findById("c1")).thenReturn(java.util.Optional.of(cuenta));
        when(repo.save(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.activarCuenta("c1"));
        assertEquals(Cuenta.EstadoCuenta.ACTIVA, cuenta.getEstado());
    }

    @Test
    @DisplayName("activarCuenta lanza excepción si no existe")
    void activarCuenta_noExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () -> service.activarCuenta("nope"));
    }

    @Test
    @DisplayName("consultarSaldo retorna saldo si existe")
    void consultarSaldo_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(123.45);
        when(repo.findById("c1")).thenReturn(java.util.Optional.of(cuenta));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertEquals(123.45, service.consultarSaldo("c1"), 0.01);
    }

    @Test
    @DisplayName("consultarSaldo retorna 0 si no existe")
    void consultarSaldo_noExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertEquals(0.0, service.consultarSaldo("nope"), 0.01);
    }

    @Test
    @DisplayName("obtenerCuentaPorCodigo retorna cuenta si existe")
    void obtenerCuentaPorCodigo_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = new Cuenta();
        cuenta.setCodigo("C-001");
        when(repo.findAll()).thenReturn(java.util.List.of(cuenta));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.obtenerCuentaPorCodigo("C-001").isPresent());
    }

    @Test
    @DisplayName("obtenerCuentaPorCodigo retorna Optional.empty si no existe")
    void obtenerCuentaPorCodigo_noExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findAll()).thenReturn(java.util.List.of());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.obtenerCuentaPorCodigo("NOPE").isEmpty());
    }

    @Test
    @DisplayName("obtenerCuentaPorNumero retorna cuenta si existe")
    void obtenerCuentaPorNumero_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = new Cuenta();
        when(repo.findByNumeroCuenta("123")).thenReturn(java.util.Optional.of(cuenta));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.obtenerCuentaPorNumero("123").isPresent());
    }

    @Test
    @DisplayName("obtenerCuentaPorNumero retorna Optional.empty si no existe")
    void obtenerCuentaPorNumero_noExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findByNumeroCuenta("nope")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.obtenerCuentaPorNumero("nope").isEmpty());
    }

    @Test
    @DisplayName("listarCuentasCliente retorna lista")
    void listarCuentasCliente_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findByClienteId("cli")).thenReturn(java.util.List.of(new Cuenta()));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertFalse(service.listarCuentasCliente("cli").isEmpty());
    }

    @Test
    @DisplayName("listarTodasCuentas retorna lista")
    void listarTodasCuentas_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findAll()).thenReturn(java.util.List.of(new Cuenta()));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertFalse(service.listarTodasCuentas().isEmpty());
    }
    @Test
    @DisplayName("abrirCuenta lanza excepción si cliente no existe")
    void abrirCuenta_clienteNoExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(clienteRepo.findById(anyString())).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () ->
            service.abrirCuenta("id", Cuenta.TipoCuenta.AHORROS, 2000)
        );
    }

    @Test
    @DisplayName("obtenerCuenta retorna Optional.empty si no existe")
    void obtenerCuenta_noExiste() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.obtenerCuenta("nope").isEmpty());
    }

    @Test
    @DisplayName("obtenerCuenta retorna cuenta si existe")
    void obtenerCuenta_existe() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = new Cuenta();
        when(repo.findById("c1")).thenReturn(java.util.Optional.of(cuenta));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.obtenerCuenta("c1").isPresent());
    }

    @Test
    @DisplayName("depositar no publica evento si falla")
    void depositar_fallaNoPublicaEvento() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = mock(Cuenta.class);
        when(repo.findById("c1")).thenReturn(java.util.Optional.of(cuenta));
        when(cuenta.depositar(100)).thenReturn(false);
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertFalse(service.depositar("c1", 100));
        verify(publisher, never()).publish(any());
    }

    @Test
    @DisplayName("retirar no publica evento si falla")
    void retirar_fallaNoPublicaEvento() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = mock(Cuenta.class);
        when(repo.findById("c1")).thenReturn(java.util.Optional.of(cuenta));
        when(cuenta.retirar(100)).thenReturn(false);
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertFalse(service.retirar("c1", 100));
        verify(publisher, never()).publish(any());
    }

    @Test
    @DisplayName("abrirCuenta llama a factory y repo correctamente")
    void abrirCuenta_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var factory = mock(com.example.factory.common.ProductoBancarioFactory.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cliente cliente = new Cliente();
        cliente.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        when(clienteRepo.findById(anyString())).thenReturn(java.util.Optional.of(cliente));
        when(config.getSaldoMinimo(anyString())).thenReturn(1000.0);
        when(fabrica.getFactory(any())).thenReturn(factory);
        Cuenta cuenta = new Cuenta();
        when(factory.crearCuenta(anyString(), any(), anyDouble())).thenReturn(cuenta);
        when(repo.save(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));
        when(clienteRepo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        Cuenta creada = service.abrirCuenta("id", Cuenta.TipoCuenta.AHORROS, 2000);
        assertNotNull(creada);
    }

    @Test
    @DisplayName("depositar y retirar funcionan y publican evento")
    void depositarYRetirar_ok() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cuenta cuenta = mock(Cuenta.class);
        when(repo.findById("c1")).thenReturn(java.util.Optional.of(cuenta));
        when(cuenta.depositar(100)).thenReturn(true);
        when(cuenta.retirar(50)).thenReturn(true);
        when(repo.save(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertTrue(service.depositar("c1", 100));
        assertTrue(service.retirar("c1", 50));
    }

    @Test
    @DisplayName("depositar lanza excepción si cuenta no existe")
    void depositar_lanzaExcepcion() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () -> service.depositar("nope", 100));
    }

    @Test
    @DisplayName("retirar lanza excepción si cuenta no existe")
    void retirar_lanzaExcepcion() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () -> service.retirar("nope", 100));
    }

    @Test
    @DisplayName("abrirCuenta lanza excepción si saldo insuficiente")
    void abrirCuenta_saldoInsuficiente() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var factory = mock(com.example.factory.common.ProductoBancarioFactory.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(config.getSaldoMinimo(anyString())).thenReturn(1000.0);
        Cliente cliente = new Cliente();
        cliente.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        when(clienteRepo.findById(anyString())).thenReturn(java.util.Optional.of(cliente));
        when(fabrica.getFactory(any())).thenReturn(factory);
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () ->
            service.abrirCuenta("id", Cuenta.TipoCuenta.AHORROS, 500)
        );
    }

    @Test
    @DisplayName("abrirCuenta lanza excepción si tipoCliente es nulo")
    void abrirCuenta_tipoClienteNulo() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        Cliente cliente = new Cliente();
        cliente.setTipoCliente(null);
        when(clienteRepo.findById(anyString())).thenReturn(java.util.Optional.of(cliente));
        when(config.getSaldoMinimo(anyString())).thenReturn(1000.0);
        when(fabrica.getFactory(isNull())).thenThrow(new IllegalArgumentException("Tipo de cliente requerido"));
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        assertThrows(IllegalArgumentException.class, () ->
            service.abrirCuenta("id", Cuenta.TipoCuenta.AHORROS, 2000)
        );
    }

    @Test
    @DisplayName("abrirCuenta llama a repo y asigna datos")
    void abrirCuenta_unit() {
        var repo = mock(com.example.repository.cuenta.CuentaRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
    var fabrica = mock(com.example.factory.common.FabricaProductosProvider.class);
    var factory = mock(com.example.factory.common.ProductoBancarioFactory.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        when(config.getSaldoMinimo(anyString())).thenReturn(1000.0);
        when(repo.save(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));
        Cliente cliente = new Cliente();
        cliente.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        when(clienteRepo.findById(anyString())).thenReturn(java.util.Optional.of(cliente));
        when(fabrica.getFactory(any())).thenReturn(factory);
        when(factory.crearCuenta(anyString(), any(), anyDouble())).thenAnswer(i -> {
            Cuenta c = new Cuenta();
            c.setTipoCuenta(i.getArgument(1));
            c.setSaldo(i.getArgument(2));
            return c;
        });
        CuentaService service = new CuentaService(repo, clienteRepo, config, fabrica, publisher);
        Cuenta cuenta = service.abrirCuenta("id", Cuenta.TipoCuenta.AHORROS, 2000);
        assertEquals(Cuenta.TipoCuenta.AHORROS, cuenta.getTipoCuenta());
        assertEquals(2000, cuenta.getSaldo(), 0.01);
    }
}
