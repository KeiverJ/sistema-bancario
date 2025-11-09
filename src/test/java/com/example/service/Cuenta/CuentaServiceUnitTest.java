package com.example.service.Cuenta;

import com.example.model.cliente.Cliente;
import com.example.model.cuenta.Cuenta;
import com.example.service.cuenta.CuentaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CuentaService (con mocks).
 */
class CuentaServiceUnitTest {
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
