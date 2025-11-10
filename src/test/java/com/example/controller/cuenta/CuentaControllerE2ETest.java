package com.example.controller.cuenta;

import com.example.service.cuenta.CuentaService;
import com.example.service.cliente.ClienteService;
import com.example.controller.CuentaController;
import com.example.controller.InputUtils;
import com.example.model.cuenta.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Scanner;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas E2E para CuentaController usando Mockito para simular entradas estáticas.
 */
class CuentaControllerE2ETest {
    /**
     * Prueba E2E: retiro exitoso.
     */
    @Test
    @DisplayName("E2E: Retiro exitoso")
    void testRetirarE2E() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-e2e");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-e2e");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(70.0);
            doNothing().when(cuentaService).retirar(anyString(), anyDouble());
            controller.retirar(scanner);
            verify(cuentaService).retirar(anyString(), anyDouble());
        }
    }

    /**
     * Prueba E2E: excepción en retiro.
     */
    @Test
    @DisplayName("E2E: Excepción en retiro")
    void testRetirarE2EExcepcion() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cliente"), eq(true))).thenReturn("cli-e2e2");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-e2e2");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(70.0);
            doThrow(new RuntimeException("Error ret")).when(cuentaService).retirar(anyString(), anyDouble());
            controller.retirar(scanner);
            verify(cuentaService).retirar(anyString(), anyDouble());
        }
    }

    /**
     * Prueba E2E: consulta de saldo exitosa.
     */
    @Test
    @DisplayName("E2E: Consulta de saldo exitosa")
    void testConsultarSaldoE2E() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-e2e3");
            Cuenta cuenta = new Cuenta();
            cuenta.setCodigo("c-e2e3");
            cuenta.setSaldo(3333.33);
            when(cuentaService.obtenerCuenta("c-e2e3")).thenReturn(java.util.Optional.of(cuenta));
            controller.consultarSaldo(scanner);
            verify(cuentaService).obtenerCuenta("c-e2e3");
        }
    }

    /**
     * Prueba E2E: consulta de saldo cuenta no encontrada.
     */
    @Test
    @DisplayName("E2E: Consulta de saldo cuenta no encontrada")
    void testConsultarSaldoE2ECuentaNoEncontrada() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta"), eq(true))).thenReturn("c-e2e4");
            when(cuentaService.obtenerCuenta("c-e2e4")).thenReturn(java.util.Optional.empty());
            controller.consultarSaldo(scanner);
            verify(cuentaService).obtenerCuenta("c-e2e4");
        }
    }
    /**
     * Prueba E2E: cuenta origen nula en transferencia.
     */
    @Test
    @DisplayName("E2E: Cuenta origen nula en transferencia")
    void testTransferirCuentaOrigenNula() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn(null);
            controller.transferir(scanner);
            verify(cuentaService, never()).transferir(anyString(), anyString(), anyDouble());
        }
    }

    /**
     * Prueba E2E: cuenta destino nula en transferencia.
     */
    @Test
    @DisplayName("E2E: Cuenta destino nula en transferencia")
    void testTransferirCuentaDestinoNula() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta destino"), eq(true))).thenReturn(null);
            controller.transferir(scanner);
            verify(cuentaService, never()).transferir(anyString(), anyString(), anyDouble());
        }
    }

    /**
     * Prueba E2E: monto nulo en transferencia.
     */
    @Test
    @DisplayName("E2E: Monto nulo en transferencia")
    void testTransferirMontoNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta destino"), eq(true))).thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a transferir"))).thenReturn(null);
            controller.transferir(scanner);
            verify(cuentaService, never()).transferir(anyString(), anyString(), anyDouble());
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
     * Prueba E2E para transferencia entre cuentas usando entradas simuladas.
     */
    @Test
    @DisplayName("E2E: Transferencia entre cuentas exitosa")
    void testTransferirE2E() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Código de cuenta origen"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Código de cuenta destino"), eq(true))).thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), eq("Monto a transferir"))).thenReturn(200.0);
            doNothing().when(cuentaService).transferir(anyString(), anyString(), anyDouble());
            controller.transferir(scanner);
            verify(cuentaService).transferir(anyString(), anyString(), anyDouble());
        }
    }
}
