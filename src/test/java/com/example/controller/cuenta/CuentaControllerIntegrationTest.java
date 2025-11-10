
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
 * Pruebas de integración para CuentaController usando Mockito para simular
 * entradas estáticas.
 */
class CuentaControllerIntegrationTest {
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

    @Test
    @DisplayName("Integration: Retiro exitoso")
    void testRetirarExitoso() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-20");
            cliente.agregarCuenta("c-20");
                when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
                when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-20");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-20");
            when(cuentaService.obtenerCuenta("c-20")).thenReturn(Optional.of(new Cuenta()));
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(80.0);
            doNothing().when(cuentaService).retirar("c-20", 80.0);
            controller.retirar(scanner);
            verify(cuentaService).retirar("c-20", 80.0);
        }
    }

    @Test
    @DisplayName("Integration: Excepción en retiro")
    void testRetirarExcepcion() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-21");
            cliente.agregarCuenta("c-21");
                when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
                when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-21");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-21");
            when(cuentaService.obtenerCuenta("c-21")).thenReturn(Optional.of(new Cuenta()));
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(80.0);
            doThrow(new RuntimeException("Error ret")).when(cuentaService).retirar("c-21", 80.0);
            controller.retirar(scanner);
            verify(cuentaService).retirar("c-21", 80.0);
        }
    }

    @Test
    @DisplayName("Integration: Consulta de saldo exitosa")
    void testConsultarSaldoExitoso() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-22");
            Cuenta cuenta = new Cuenta();
            cuenta.setCodigo("c-22");
            cuenta.setSaldo(2222.22);
            when(cuentaService.obtenerCuenta("c-22")).thenReturn(Optional.of(cuenta));
            controller.consultarSaldo(scanner);
            verify(cuentaService).obtenerCuenta("c-22");
        }
    }

    @Test
    @DisplayName("Integration: Consulta de saldo cuenta no encontrada")
    void testConsultarSaldoCuentaNoEncontrada() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-23");
            when(cuentaService.obtenerCuenta("c-23")).thenReturn(Optional.empty());
            controller.consultarSaldo(scanner);
            verify(cuentaService).obtenerCuenta("c-23");
        }
    }

    @Test
    @DisplayName("Integration: Cliente no encontrado en depósito")
    void testDepositarClienteNoEncontrado() {
        try (var mocked = mockStatic(InputUtils.class)) {
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(new Cliente()));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.empty());
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-x");
            controller.depositar(scanner);
            verify(cuentaService, never()).depositar(anyString(), anyDouble());
        }
    }

    @Test
    @DisplayName("Integration: Cliente sin cuentas en depósito")
    void testDepositarClienteSinCuentas() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-3");
                when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
                when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-3");
            controller.depositar(scanner);
            verify(cuentaService, never()).depositar(anyString(), anyDouble());
        }
    }

    @Test
    @DisplayName("Integration: Cuenta no encontrada en depósito")
    void testDepositarCuentaNoEncontrada() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-4");
            cliente.agregarCuenta("c-4");
                when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
                when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-4");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-4");
            when(cuentaService.obtenerCuenta(anyString())).thenReturn(Optional.empty());
            controller.depositar(scanner);
            verify(cuentaService, never()).depositar(anyString(), anyDouble());
        }
    }

    @Test
    @DisplayName("Integration: Monto nulo en depósito")
    void testDepositarMontoNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-5");
            cliente.agregarCuenta("c-5");
                when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
                when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-5");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-5");
            when(cuentaService.obtenerCuenta(anyString())).thenReturn(Optional.of(new Cuenta()));
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a depositar"))).thenReturn(null);
            controller.depositar(scanner);
            verify(cuentaService, never()).depositar(anyString(), anyDouble());
        }
    }

    @Test
    @DisplayName("Integration: Depósito en cuenta exitoso")
    void testDepositar() {
        try (var mocked = mockStatic(InputUtils.class)) {
            Cliente cliente = new Cliente();
            cliente.setId("cli-2");
            cliente.agregarCuenta("c-2");
            when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Código de cliente"), eq(true)))
                    .thenReturn("cli-2");
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Código de cuenta"), eq(true)))
                    .thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), eq("Monto a depositar"))).thenReturn(500.0);
            Cuenta cuenta = new Cuenta();
            cuenta.setCodigo("c-2");
            cuenta.setSaldo(1500.0);
            when(cuentaService.obtenerCuenta(anyString())).thenReturn(Optional.of(cuenta));
            doNothing().when(cuentaService).depositar(anyString(), anyDouble());
            controller.depositar(scanner);
            verify(cuentaService).depositar(anyString(), anyDouble());
        }
    }
}
