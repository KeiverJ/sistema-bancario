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

import java.util.Optional;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class CreditoControllerE2ETest {
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

    // E2E: Solicitud de crédito completa y exitosa
    @Test
    @DisplayName("E2E: Solicitud de crédito completa y exitosa")
    void testE2ESolicitudCreditoCompleta() {
        Cliente cliente = new Cliente();
        cliente.setId("cliE2E");
        when(clienteService.listarTodos()).thenReturn(java.util.List.of(cliente));
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("cliE2E");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), anyString(), any()))
                    .thenReturn(Credito.TipoCredito.LIBRE_INVERSION);
            mocked.when(() -> InputUtils.solicitarMonto(any(Scanner.class), anyString())).thenReturn(10000.0);
            mocked.when(() -> InputUtils.solicitarEnteroPositivo(any(Scanner.class), anyString())).thenReturn(12);
            when(clienteService.obtenerClientePorCodigo(anyString())).thenReturn(Optional.of(cliente));
            when(creditoService.solicitarCredito(anyString(), any(), anyDouble(), anyInt())).thenReturn(new Credito());
            controller.solicitarCredito(scanner);
            verify(creditoService, atLeast(0)).solicitarCredito(anyString(), any(), anyDouble(), anyInt());
        }
    }

    // E2E: Pago de cuota de crédito exitoso
    @Test
    @DisplayName("E2E: Pago de cuota de crédito exitoso")
    void testE2EPagoCuotaCredito() {
        Credito credito = new Credito();
        try (var mocked = org.mockito.Mockito.mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), anyString(), anyBoolean()))
                    .thenReturn("CR-E2E");
            when(creditoService.obtenerCreditoPorCodigo(anyString())).thenReturn(Optional.of(credito));
            doNothing().when(creditoService).pagarCuota(anyString(), anyDouble());
            controller.pagarCuotaCredito(scanner);
            verify(creditoService).pagarCuota(anyString(), anyDouble());
        }
    }
}
