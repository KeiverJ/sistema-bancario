package com.example.controller.transaccion;

import com.example.service.transaccion.TransaccionService;
import com.example.controller.InputUtils;
import com.example.controller.TransaccionController;
import com.example.service.cuenta.CuentaService;
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
 * Pruebas unitarias para TransaccionController usando Mockito para simular entradas estáticas.
 */
class TransaccionControllerUnitTest {
    /**
     * Prueba: monto nulo en depósito.
     */
    @Test
    @DisplayName("Unit: Monto nulo en depósito")
    void testDepositoMontoNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta destino"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a depositar"))).thenReturn(null);
            when(scanner.nextLine()).thenReturn("1");
            controller.crearTransaccion(scanner);
            verify(transaccionService, never()).crearDeposito(anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba: monto negativo en depósito.
     */
    @Test
    @DisplayName("Unit: Monto negativo en depósito")
    void testDepositoMontoNegativo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta destino"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a depositar"))).thenReturn(-100.0);
            when(scanner.nextLine()).thenReturn("1");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearDeposito(eq("c-1"), eq(-100.0), isNull());
        }
    }

    /**
     * Prueba: excepción del servicio en depósito.
     */
    @Test
    @DisplayName("Unit: Excepción en crearDeposito")
    void testDepositoExcepcionServicio() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta destino"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a depositar"))).thenReturn(200.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Descripción (opcional)"), eq(false))).thenReturn("Depósito");
            when(scanner.nextLine()).thenReturn("1");
            when(transaccionService.crearDeposito(anyString(), anyDouble(), anyString())).thenThrow(new RuntimeException("Error"));
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearDeposito(anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba: código de cuenta nulo en retiro.
     */
    @Test
    @DisplayName("Unit: Código de cuenta nulo en retiro")
    void testRetiroCuentaNula() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn(null);
            when(scanner.nextLine()).thenReturn("2");
            controller.crearTransaccion(scanner);
            verify(transaccionService, never()).crearRetiro(anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba: excepción del servicio en retiro.
     */
    @Test
    @DisplayName("Unit: Excepción en crearRetiro")
    void testRetiroExcepcionServicio() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(100.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Ubicación cajero/sucursal (opcional)"), eq(false))).thenReturn("Sucursal");
            when(scanner.nextLine()).thenReturn("2");
            doThrow(new RuntimeException("Error")).when(transaccionService).crearRetiro(anyString(), anyDouble(), anyString());
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearRetiro(anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba: excepción del servicio en transferencia.
     */
    @Test
    @DisplayName("Unit: Excepción en crearTransferencia")
    void testTransferenciaExcepcionServicio() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta destino"), eq(true))).thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a transferir"))).thenReturn(400.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Descripción (opcional)"), eq(false))).thenReturn("Pago amigo");
            when(scanner.nextLine()).thenReturn("3");
            doThrow(new RuntimeException("Error")).when(transaccionService).crearTransferencia(anyString(), anyString(), anyDouble(), anyString());
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearTransferencia(anyString(), anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba: excepción del servicio en pago de servicio.
     */
    @Test
    @DisplayName("Unit: Excepción en crearPagoServicio")
    void testPagoServicioExcepcionServicio() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-3");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de servicio"), eq(true))).thenReturn("serv-1");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a pagar"))).thenReturn(50.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Referencia de pago"), eq(true))).thenReturn("ref-123");
            when(scanner.nextLine()).thenReturn("4");
            doThrow(new RuntimeException("Error")).when(transaccionService).crearPagoServicio(anyString(), anyString(), anyDouble(), anyString());
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearPagoServicio(anyString(), anyString(), anyDouble(), anyString());
        }
    }
    /**
     * Prueba: crear retiro exitoso.
     */
    @Test
    @DisplayName("Unit: Crear retiro exitoso")
    void testCrearRetiro() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(150.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Ubicación cajero/sucursal (opcional)"), eq(false))).thenReturn("Sucursal Centro");
            doReturn(null).when(transaccionService).crearRetiro(anyString(), anyDouble(), anyString());
            when(scanner.nextLine()).thenReturn("2");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearRetiro(anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba: crear transferencia exitosa.
     */
    @Test
    @DisplayName("Unit: Crear transferencia exitosa")
    void testCrearTransferencia() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta destino"), eq(true))).thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a transferir"))).thenReturn(400.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Descripción (opcional)"), eq(false))).thenReturn("Pago amigo");
            doReturn(null).when(transaccionService).crearTransferencia(anyString(), anyString(), anyDouble(), anyString());
            when(scanner.nextLine()).thenReturn("3");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearTransferencia(anyString(), anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba: crear pago de servicio exitoso.
     */
    @Test
    @DisplayName("Unit: Crear pago de servicio exitoso")
    void testCrearPagoServicio() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-3");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de servicio"), eq(true))).thenReturn("serv-1");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a pagar"))).thenReturn(50.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Referencia de pago"), eq(true))).thenReturn("ref-123");
            doReturn(null).when(transaccionService).crearPagoServicio(anyString(), anyString(), anyDouble(), anyString());
            when(scanner.nextLine()).thenReturn("4");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearPagoServicio(anyString(), anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba: opción inválida.
     */
    @Test
    @DisplayName("Unit: Opción inválida en crearTransaccion")
    void testOpcionInvalida() {
        when(scanner.nextLine()).thenReturn("99");
        controller.crearTransaccion(scanner);
        verifyNoInteractions(transaccionService);
    }

    /**
     * Prueba: cancelar operación.
     */
    @Test
    @DisplayName("Unit: Cancelar operación en crearTransaccion")
    void testCancelarOperacion() {
        when(scanner.nextLine()).thenReturn("0");
        controller.crearTransaccion(scanner);
        verifyNoInteractions(transaccionService);
    }
    @Mock
    private TransaccionService transaccionService;
    @Mock
    private CuentaService cuentaService;
    @Mock
    private Scanner scanner;
    @InjectMocks
    private TransaccionController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba unitaria para crear un depósito usando entradas simuladas.
     */
    @Test
    @DisplayName("Unit: Crear depósito exitoso")
    void testCrearDeposito() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Código de cuenta destino"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), eq("Monto a depositar"))).thenReturn(300.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Descripción (opcional)"), eq(false))).thenReturn("Depósito inicial");
            // Simular el retorno de una transacción al crear depósito
            when(transaccionService.crearDeposito(anyString(), anyDouble(), anyString())).thenReturn(null);
            // Simular selección de opción "1" para depósito
            when(scanner.nextLine()).thenReturn("1");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearDeposito(anyString(), anyDouble(), anyString());
        }
    }
}
