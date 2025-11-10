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

import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class CreditoControllerUnitTest {
    // Verifica que no se solicita crédito si tipo es nulo
    @Test
    @DisplayName("No solicita crédito si tipo es nulo")
    void testSolicitarCreditoTipoNulo() {
        Cliente cliente = new Cliente();
        cliente.setId("cli2");
        when(clienteService.listarTodos()).thenReturn(Collections.singletonList(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("cli2");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), anyString(), any())).thenReturn(null);
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            controller.solicitarCredito(scanner);
            verify(creditoService, never()).solicitarCredito(anyString(), any(), anyDouble(), anyInt());
        }
    }

    // Verifica que no se solicita crédito si monto es nulo
    @Test
    @DisplayName("No solicita crédito si monto es nulo")
    void testSolicitarCreditoMontoNulo() {
        Cliente cliente = new Cliente();
        cliente.setId("cli3");
        when(clienteService.listarTodos()).thenReturn(Collections.singletonList(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("cli3");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), anyString(), any()))
                    .thenReturn(Credito.TipoCredito.LIBRE_INVERSION);
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(null);
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            controller.solicitarCredito(scanner);
            verify(creditoService, never()).solicitarCredito(anyString(), any(), anyDouble(), anyInt());
        }
    }

    // Verifica que no se solicita crédito si plazo es nulo
    @Test
    @DisplayName("No solicita crédito si plazo es nulo")
    void testSolicitarCreditoPlazoNulo() {
        Cliente cliente = new Cliente();
        cliente.setId("cli4");
        when(clienteService.listarTodos()).thenReturn(Collections.singletonList(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("cli4");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), anyString(), any()))
                    .thenReturn(Credito.TipoCredito.LIBRE_INVERSION);
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(1000.0);
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

    // Verifica que no se solicita crédito si no hay clientes
    @Test
    @DisplayName("No solicita crédito si no hay clientes")
    void testSolicitarCreditoSinClientes() {
        when(clienteService.listarTodos()).thenReturn(Collections.emptyList());
        controller.solicitarCredito(scanner);
        verify(clienteService, times(1)).listarTodos();
        verifyNoMoreInteractions(creditoService);
    }

    // Verifica que muestra error si el cliente no existe
    @Test
    @DisplayName("Error si el cliente no existe")
    void testSolicitarCreditoClienteNoExiste() {
        when(clienteService.listarTodos()).thenReturn(Collections.singletonList(new Cliente()));
        // Simula que InputUtils.solicitarTexto devuelve "C1"
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("C1");
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.empty());
            controller.solicitarCredito(scanner);
            verify(clienteService).obtenerClientePorCodigo("C1");
        }
    }

    // Verifica que lista créditos de un cliente existente
    @Test
    @DisplayName("Lista créditos de cliente existente")
    void testListarCreditosCliente() {
        Cliente cliente = new Cliente();
        cliente.setId("cli1");
        Credito credito = mock(Credito.class);
        when(credito.getCodigo()).thenReturn("CR-1");
        when(credito.getTipoCredito()).thenReturn(Credito.TipoCredito.LIBRE_INVERSION);
        when(credito.getMonto()).thenReturn(10000.0);
        when(credito.getSaldo()).thenReturn(5000.0);
        when(credito.getTasaInteres()).thenReturn(12.5);
    when(credito.getEstadoActual()).thenReturn(Credito.EstadoCredito.ACTIVO);
        when(clienteService.listarTodos()).thenReturn(Collections.singletonList(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("cli1");
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            when(creditoService.listarCreditosCliente("cli1")).thenReturn(Collections.singletonList(credito));
            controller.listarCreditosCliente(scanner);
            verify(creditoService).listarCreditosCliente("cli1");
        }
    }

    // Verifica que muestra mensaje si el cliente no tiene créditos
    @Test
    @DisplayName("Muestra mensaje si el cliente no tiene créditos")
    void testListarCreditosClienteSinCreditos() {
        Cliente cliente = new Cliente();
        cliente.setId("cli2");
        when(clienteService.listarTodos()).thenReturn(Collections.singletonList(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("cli2");
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            when(creditoService.listarCreditosCliente("cli2")).thenReturn(Collections.emptyList());
            controller.listarCreditosCliente(scanner);
            verify(creditoService).listarCreditosCliente("cli2");
        }
    }

    // Verifica que muestra error si el cliente no existe al listar créditos
    @Test
    @DisplayName("Error si el cliente no existe al listar créditos")
    void testListarCreditosClienteNoExiste() {
        when(clienteService.listarTodos()).thenReturn(Collections.singletonList(new Cliente()));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("C2");
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.empty());
            controller.listarCreditosCliente(scanner);
            verify(clienteService).obtenerClientePorCodigo("C2");
        }
    }

    // Verifica que paga cuota correctamente
    @Test
    @DisplayName("Paga cuota correctamente")
    void testPagarCuotaCreditoExito() {
        Credito credito = mock(Credito.class);
        when(credito.getSaldo()).thenReturn(2000.0);
        when(creditoService.obtenerCreditoPorCodigo(anyString())).thenReturn(Optional.of(credito));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("CR-2");
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(500.0);
            controller.pagarCuotaCredito(scanner);
            verify(creditoService).pagarCuota("CR-2", 500.0);
        }
    }

    // Verifica que no paga cuota si monto es nulo
    @Test
    @DisplayName("No paga cuota si monto es nulo")
    void testPagarCuotaCreditoMontoNulo() {
        Credito credito = mock(Credito.class);
        when(credito.getSaldo()).thenReturn(2000.0);
        when(creditoService.obtenerCreditoPorCodigo(anyString())).thenReturn(Optional.of(credito));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("CR-3");
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(null);
            controller.pagarCuotaCredito(scanner);
            verify(creditoService, never()).pagarCuota(anyString(), anyDouble());
        }
    }

    // Verifica que maneja excepción al pagar cuota
    @Test
    @DisplayName("Maneja excepción al pagar cuota")
    void testPagarCuotaCreditoConExcepcion() {
        Credito credito = mock(Credito.class);
        when(credito.getSaldo()).thenReturn(2000.0);
        when(creditoService.obtenerCreditoPorCodigo(anyString())).thenReturn(Optional.of(credito));
        doThrow(new RuntimeException("Error de pago")).when(creditoService).pagarCuota(anyString(), anyDouble());
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("CR-4");
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(100.0);
            controller.pagarCuotaCredito(scanner);
            verify(creditoService).pagarCuota("CR-4", 100.0);
        }
    }

    // Verifica que muestra error si el crédito no existe al pagar cuota
    @Test
    @DisplayName("Error si el crédito no existe al pagar cuota")
    void testPagarCuotaCreditoNoExiste() {
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("CR-1");
            when(creditoService.obtenerCreditoPorCodigo(anyString())).thenReturn(Optional.empty());
            controller.pagarCuotaCredito(scanner);
            verify(creditoService).obtenerCreditoPorCodigo("CR-1");
        }
    }
}
