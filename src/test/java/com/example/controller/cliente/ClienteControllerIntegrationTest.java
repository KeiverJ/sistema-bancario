
package com.example.controller.cliente;

import com.example.controller.ClienteController;
import com.example.controller.InputUtils;
import com.example.model.cliente.Cliente;
import com.example.service.cliente.ClienteService;
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
 * Pruebas de integración para ClienteController usando Mockito para simular entradas estáticas.
 */
class ClienteControllerIntegrationTest {
    /**
     * Prueba integración: nombre nulo al crear cliente.
     */
    @Test
    @DisplayName("Integration: Nombre nulo en crearCliente")
    void testCrearClienteNombreNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn(null);
            controller.crearCliente(scanner);
            verify(clienteService, never()).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba integración: tipo de documento nulo al crear cliente.
     */
    @Test
    @DisplayName("Integration: Tipo de documento nulo en crearCliente")
    void testCrearClienteTipoDocNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn("Ana");
            mocked.when(() -> InputUtils.solicitarOpcion(any(), eq("Tipo de documento"), any(String[].class))).thenReturn(null);
            controller.crearCliente(scanner);
            verify(clienteService, never()).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba integración: número de documento nulo al crear cliente.
     */
    @Test
    @DisplayName("Integration: Número de documento nulo en crearCliente")
    void testCrearClienteNumDocNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn("Ana");
            mocked.when(() -> InputUtils.solicitarOpcion(any(), eq("Tipo de documento"), any(String[].class))).thenReturn("CC");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Número de documento"), eq(true))).thenReturn(null);
            controller.crearCliente(scanner);
            verify(clienteService, never()).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba integración: tipo de cliente nulo al crear cliente.
     */
    @Test
    @DisplayName("Integration: Tipo de cliente nulo en crearCliente")
    void testCrearClienteTipoClienteNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn("Ana");
            mocked.when(() -> InputUtils.solicitarOpcion(any(), eq("Tipo de documento"), any(String[].class))).thenReturn("CC");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Número de documento"), eq(true))).thenReturn("123");
            mocked.when(() -> InputUtils.solicitarEnum(any(), eq("Tipo de cliente"), eq(Cliente.TipoCliente.class))).thenReturn(null);
            controller.crearCliente(scanner);
            verify(clienteService, never()).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }
    @Mock
    private ClienteService clienteService;
    @Mock
    private Scanner scanner;
    @InjectMocks
    private ClienteController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba de integración para la creación de un cliente con entradas simuladas.
     */
    @Test
    @DisplayName("Integration: Creación de cliente exitosa")
    void testCrearClienteIntegration() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Nombre completo"), eq(true))).thenReturn("Ana Lopez");
            mocked.when(() -> InputUtils.solicitarOpcion(any(Scanner.class), eq("Tipo de documento"), any(String[].class))).thenReturn("NIT");
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Número de documento"), eq(true))).thenReturn("654321");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), eq("Tipo de cliente"), eq(Cliente.TipoCliente.class))).thenReturn(Cliente.TipoCliente.PERSONA_JURIDICA);
            Cliente clienteMock = new Cliente();
            clienteMock.setCodigo("CLI-0002");
            clienteMock.setTipoCliente(Cliente.TipoCliente.PERSONA_JURIDICA);
            when(clienteService.crearCliente(anyString(), anyString(), anyString(), any())).thenReturn(clienteMock);
            controller.crearCliente(scanner);
            verify(clienteService).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }
}
