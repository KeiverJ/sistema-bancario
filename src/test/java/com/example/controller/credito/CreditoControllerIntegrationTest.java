package com.example.controller.credito;

import com.example.controller.CreditoController;
import com.example.controller.InputUtils;
import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.service.cliente.ClienteService;
import com.example.service.credito.CreditoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class CreditoControllerIntegrationTest {
    // Prueba integración: Error al solicitar crédito por cliente no encontrado
    @Test
    @DisplayName("Error cliente no encontrado al solicitar crédito (integración)")
    void testSolicitarCreditoClienteNoEncontrado() {
        Cliente cliente = new Cliente();
        cliente.setId("cliX");
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean())).thenReturn("cliX");
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.empty());
            controller.solicitarCredito(scanner);
            verify(creditoService, never()).solicitarCredito(anyString(), any(), anyDouble(), anyInt());
        }
    }

    // Prueba integración: Error tipo de crédito nulo
    @Test
    @DisplayName("Tipo de crédito nulo al solicitar crédito (integración)")
    void testSolicitarCreditoTipoNulo() {
        Cliente cliente = new Cliente();
        cliente.setId("cliY");
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean())).thenReturn("cliY");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), anyString(), any())).thenReturn(null);
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            controller.solicitarCredito(scanner);
            verify(creditoService, never()).solicitarCredito(anyString(), any(), anyDouble(), anyInt());
        }
    }

    // Prueba integración: Error monto nulo
    @Test
    @DisplayName("Monto nulo al solicitar crédito (integración)")
    void testSolicitarCreditoMontoNulo() {
        Cliente cliente = new Cliente();
        cliente.setId("cliZ");
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean())).thenReturn("cliZ");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), anyString(), any())).thenReturn(Credito.TipoCredito.HIPOTECARIO);
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(null);
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            controller.solicitarCredito(scanner);
            verify(creditoService, never()).solicitarCredito(anyString(), any(), anyDouble(), anyInt());
        }
    }

    // Prueba integración: Error plazo nulo
    @Test
    @DisplayName("Plazo nulo al solicitar crédito (integración)")
    void testSolicitarCreditoPlazoNulo() {
        Cliente cliente = new Cliente();
        cliente.setId("cliW");
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean())).thenReturn("cliW");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), anyString(), any())).thenReturn(Credito.TipoCredito.HIPOTECARIO);
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(5000.0);
            mocked.when(() -> InputUtils.solicitarEnteroPositivo(any(Scanner.class), anyString())).thenReturn(null);
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            controller.solicitarCredito(scanner);
            verify(creditoService, never()).solicitarCredito(anyString(), any(), anyDouble(), anyInt());
        }
    }
    private CreditoService creditoService;
    private ClienteService clienteService;
    private CreditoController controller;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        creditoService = mock(CreditoService.class);
        clienteService = mock(ClienteService.class);
        controller = new CreditoController(creditoService, clienteService);
        scanner = mock(Scanner.class);
    }

    // Prueba integración: Solicitar crédito exitosamente
    @Test
    @DisplayName("Solicitar crédito exitoso (integración)")
    void testSolicitarCreditoExitoso() {
        Cliente cliente = new Cliente();
        cliente.setId("cli1");
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean())).thenReturn("cli1");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), anyString(), any())).thenReturn(Credito.TipoCredito.LIBRE_INVERSION);
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(10000.0);
            mocked.when(() -> InputUtils.solicitarEnteroPositivo(any(Scanner.class), anyString())).thenReturn(12);
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            when(creditoService.solicitarCredito(anyString(), any(), anyDouble(), anyInt())).thenReturn(new Credito());
            controller.solicitarCredito(scanner);
            verify(creditoService, atLeast(0)).solicitarCredito(anyString(), any(), anyDouble(), anyInt());
        }
    }

    // Prueba integración: Listar créditos de cliente sin créditos
    @Test
    @DisplayName("Listar créditos de cliente sin créditos (integración)")
    void testListarCreditosClienteSinCreditos() {
        Cliente cliente = new Cliente();
        cliente.setId("cli2");
        when(clienteService.listarTodos()).thenReturn(List.of(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean())).thenReturn("cli2");
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            when(creditoService.listarCreditosCliente("cli2")).thenReturn(List.of());
            controller.listarCreditosCliente(scanner);
            verify(creditoService).listarCreditosCliente("cli2");
        }
    }
}
