package com.example.controller.cuenta;

import com.example.model.cuenta.Cuenta;
import com.example.controller.CuentaController;
import com.example.controller.InputUtils;
import com.example.model.cliente.Cliente;
import com.example.service.cuenta.CuentaService;
import com.example.service.cliente.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Scanner;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CuentaController usando Mockito para simular entradas estáticas.
 */
class CuentaControllerUnitTest {
    /**
     * Prueba: impresión de cuentas del cliente (con cuentas).
     */
    @Test
    @DisplayName("Unit: Imprime cuentas del cliente con cuentas")
    void testListarCuentasClienteConCuentas() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-6");
            cliente.setNombre("ConCuentas");
            cliente.agregarCuenta("c-1");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-6");
            Cuenta cuenta = new Cuenta();
            cuenta.setCodigo("c-1");
            cuenta.setNumeroCuenta("123456");
            cuenta.setTipoCuenta(Cuenta.TipoCuenta.AHORROS);
            cuenta.setSaldo(500.0);
            cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
            when(cuentaService.obtenerCuenta("c-1")).thenReturn(Optional.of(cuenta));
            controller.listarCuentasCliente(scanner);
            verify(cuentaService).obtenerCuenta("c-1");
        }
    }

    /**
     * Prueba: depósito exitoso.
     */
    @Test
    @DisplayName("Unit: Depósito exitoso")
    void testDepositarExitoso() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-7");
            cliente.agregarCuenta("c-7");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-7");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-7");
            when(cuentaService.obtenerCuenta("c-7")).thenReturn(Optional.of(new Cuenta()));
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a depositar"))).thenReturn(100.0);
            when(cuentaService.depositar("c-7", 100.0)).thenReturn(true);
            controller.depositar(scanner);
            verify(cuentaService).depositar("c-7", 100.0);
        }
    }

    /**
     * Prueba: excepción en depósito.
     */
    @Test
    @DisplayName("Unit: Excepción en depósito")
    void testDepositarExcepcion() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-8");
            cliente.agregarCuenta("c-8");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-8");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-8");
            when(cuentaService.obtenerCuenta("c-8")).thenReturn(Optional.of(new Cuenta()));
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a depositar"))).thenReturn(100.0);
            doThrow(new RuntimeException("Error dep")).when(cuentaService).depositar("c-8", 100.0);
            controller.depositar(scanner);
            verify(cuentaService).depositar("c-8", 100.0);
        }
    }

    /**
     * Prueba: retiro exitoso.
     */
    @Test
    @DisplayName("Unit: Retiro exitoso")
    void testRetirarExitoso() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-9");
            cliente.agregarCuenta("c-9");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-9");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-9");
            when(cuentaService.obtenerCuenta("c-9")).thenReturn(Optional.of(new Cuenta()));
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(50.0);
            when(cuentaService.retirar("c-9", 50.0)).thenReturn(true);
            controller.retirar(scanner);
            verify(cuentaService).retirar("c-9", 50.0);
        }
    }

    /**
     * Prueba: excepción en retiro.
     */
    @Test
    @DisplayName("Unit: Excepción en retiro")
    void testRetirarExcepcion() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-10");
            cliente.agregarCuenta("c-10");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-10");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-10");
            when(cuentaService.obtenerCuenta("c-10")).thenReturn(Optional.of(new Cuenta()));
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(50.0);
            doThrow(new RuntimeException("Error ret")).when(cuentaService).retirar("c-10", 50.0);
            controller.retirar(scanner);
            verify(cuentaService).retirar("c-10", 50.0);
        }
    }

    /**
     * Prueba: consulta de saldo exitosa.
     */
    @Test
    @DisplayName("Unit: Consulta de saldo exitosa")
    void testConsultarSaldoExitoso() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-11");
            Cuenta cuenta = new Cuenta();
            cuenta.setCodigo("c-11");
            cuenta.setSaldo(1234.56);
            when(cuentaService.obtenerCuenta("c-11")).thenReturn(Optional.of(cuenta));
            controller.consultarSaldo(scanner);
            verify(cuentaService).obtenerCuenta("c-11");
        }
    }

    /**
     * Prueba: consulta de saldo con cuenta no encontrada.
     */
    @Test
    @DisplayName("Unit: Consulta de saldo cuenta no encontrada")
    void testConsultarSaldoCuentaNoEncontrada() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-12");
            when(cuentaService.obtenerCuenta("c-12")).thenReturn(Optional.empty());
            controller.consultarSaldo(scanner);
            verify(cuentaService).obtenerCuenta("c-12");
        }
    }
    /**
     * Prueba: cliente no encontrado al abrir cuenta.
     */
    @Test
    @DisplayName("Unit: Error cliente no encontrado en apertura de cuenta")
    void testAbrirCuentaClienteNoEncontrado() {
        try (var mocked = mockStatic(InputUtils.class)) {
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(new Cliente()));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.empty());
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-x");
            controller.abrirCuenta(scanner);
            verify(clienteService).obtenerClientePorCodigo(anyString());
            verify(cuentaService, never()).abrirCuenta(anyString(), any(), anyDouble());
        }
    }

    /**
     * Prueba: tipo de cuenta nulo al abrir cuenta.
     */
    @Test
    @DisplayName("Unit: Tipo de cuenta nulo en apertura de cuenta")
    void testAbrirCuentaTipoNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-2");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-2");
            mocked.when(() -> InputUtils.solicitarEnum(any(), eq("Tipo de cuenta"), eq(Cuenta.TipoCuenta.class))).thenReturn(null);
            controller.abrirCuenta(scanner);
            verify(cuentaService, never()).abrirCuenta(anyString(), any(), anyDouble());
        }
    }

    /**
     * Prueba: saldo inicial nulo al abrir cuenta.
     */
    @Test
    @DisplayName("Unit: Saldo inicial nulo en apertura de cuenta")
    void testAbrirCuentaSaldoNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-3");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-3");
            mocked.when(() -> InputUtils.solicitarEnum(any(), eq("Tipo de cuenta"), eq(Cuenta.TipoCuenta.class))).thenReturn(Cuenta.TipoCuenta.AHORROS);
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Saldo inicial"))).thenReturn(null);
            controller.abrirCuenta(scanner);
            verify(cuentaService, never()).abrirCuenta(anyString(), any(), anyDouble());
        }
    }

    /**
     * Prueba: excepción al crear cuenta.
     */
    @Test
    @DisplayName("Unit: Excepción al crear cuenta")
    void testAbrirCuentaExcepcion() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-4");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-4");
            mocked.when(() -> InputUtils.solicitarEnum(any(), eq("Tipo de cuenta"), eq(Cuenta.TipoCuenta.class))).thenReturn(Cuenta.TipoCuenta.AHORROS);
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Saldo inicial"))).thenReturn(100.0);
            when(cuentaService.abrirCuenta(anyString(), any(), anyDouble())).thenThrow(new RuntimeException("Error DB"));
            controller.abrirCuenta(scanner);
            verify(cuentaService).abrirCuenta(anyString(), any(), anyDouble());
        }
    }

    /**
     * Prueba: listar cuentas de cliente sin cuentas.
     */
    @Test
    @DisplayName("Unit: Cliente sin cuentas en listarCuentasCliente")
    void testListarCuentasClienteSinCuentas() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-5");
            cliente.setNombre("SinCuentas");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-5");
            controller.listarCuentasCliente(scanner);
            verify(clienteService).obtenerClientePorCodigo(anyString());
        }
    }

    /**
     * Prueba: error cliente no encontrado en listarCuentasCliente.
     */
    @Test
    @DisplayName("Unit: Error cliente no encontrado en listarCuentasCliente")
    void testListarCuentasClienteNoEncontrado() {
        try (var mocked = mockStatic(InputUtils.class)) {
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(new Cliente()));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.empty());
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-x");
            controller.listarCuentasCliente(scanner);
            verify(clienteService).obtenerClientePorCodigo(anyString());
        }
    }
    @Mock
    private CuentaService cuentaService;
    @Mock
    private ClienteService clienteService;
    @Mock
    private Scanner scanner;
    @InjectMocks
    private CuentaController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba unitaria para la apertura de cuenta usando entradas simuladas.
     */
    @Test
    @DisplayName("Unit: Apertura de cuenta exitosa")
    void testAbrirCuenta() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-1");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Código de cliente"), eq(true))).thenReturn("cli-1");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), eq("Tipo de cuenta"), eq(Cuenta.TipoCuenta.class))).thenReturn(Cuenta.TipoCuenta.AHORROS);
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), eq("Saldo inicial"))).thenReturn(1000.0);
            Cuenta cuenta = new Cuenta();
            cuenta.setCodigo("C-001");
            cuenta.setNumeroCuenta("123456");
            cuenta.setSaldo(1000.0);
            when(cuentaService.abrirCuenta(anyString(), any(), anyDouble())).thenReturn(cuenta);
            controller.abrirCuenta(scanner);
            verify(cuentaService).abrirCuenta(anyString(), any(), anyDouble());
        }
    }
}
