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
 * Pruebas E2E para TransaccionController usando Mockito para simular entradas estáticas.
 */
class TransaccionControllerE2ETest {
    /**
     * Prueba E2E: opción inválida.
     */
    @Test
    @DisplayName("E2E: Opción inválida en crearTransaccion")
    void testOpcionInvalida() {
        when(scanner.nextLine()).thenReturn("99");
        controller.crearTransaccion(scanner);
        verifyNoInteractions(transaccionService);
    }

    /**
     * Prueba E2E: cancelar operación.
     */
    @Test
    @DisplayName("E2E: Cancelar operación en crearTransaccion")
    void testCancelarOperacion() {
        when(scanner.nextLine()).thenReturn("0");
        controller.crearTransaccion(scanner);
        verifyNoInteractions(transaccionService);
    }

    /**
     * Prueba E2E: crear depósito exitoso.
     */
    @Test
    @DisplayName("E2E: Crear depósito exitoso")
    void testCrearDepositoE2E() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta destino"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a depositar"))).thenReturn(300.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Descripción (opcional)"), eq(false))).thenReturn("Depósito inicial");
            doNothing().when(transaccionService).crearDeposito(anyString(), anyDouble(), anyString());
            when(scanner.nextLine()).thenReturn("1");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearDeposito(anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba E2E: crear retiro exitoso.
     */
    @Test
    @DisplayName("E2E: Crear retiro exitoso")
    void testCrearRetiroE2E() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a retirar"))).thenReturn(150.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Ubicación cajero/sucursal (opcional)"), eq(false))).thenReturn("Sucursal Centro");
            doNothing().when(transaccionService).crearRetiro(anyString(), anyDouble(), anyString());
            when(scanner.nextLine()).thenReturn("2");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearRetiro(anyString(), anyDouble(), anyString());
        }
    }

    /**
     * Prueba E2E: crear pago de servicio exitoso.
     */
    @Test
    @DisplayName("E2E: Crear pago de servicio exitoso")
    void testCrearPagoServicioE2E() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de cuenta origen"), eq(true))).thenReturn("c-3");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Código de servicio"), eq(true))).thenReturn("serv-1");
            mocked.when(() -> InputUtils.solicitarMonto(any(), eq("Monto a pagar"))).thenReturn(50.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Referencia de pago"), eq(true))).thenReturn("ref-123");
            doNothing().when(transaccionService).crearPagoServicio(anyString(), anyString(), anyDouble(), anyString());
            when(scanner.nextLine()).thenReturn("4");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearPagoServicio(anyString(), anyString(), anyDouble(), anyString());
        }
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
     * Prueba E2E para crear una transferencia usando entradas simuladas.
     */
    @Test
    @DisplayName("E2E: Crear transferencia exitosa")
    void testCrearTransferenciaE2E() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Código de cuenta origen"), eq(true))).thenReturn("c-1");
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Código de cuenta destino"), eq(true))).thenReturn("c-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), eq("Monto a transferir"))).thenReturn(400.0);
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Descripción (opcional)"), eq(false))).thenReturn("Pago amigo");
            doNothing().when(transaccionService).crearTransferencia(anyString(), anyString(), anyDouble(), anyString());
            // Simular selección de opción "3" para transferencia
            when(scanner.nextLine()).thenReturn("3");
            controller.crearTransaccion(scanner);
            verify(transaccionService).crearTransferencia(anyString(), anyString(), anyDouble(), anyString());
        }
    }
}
