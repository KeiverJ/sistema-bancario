
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
 * Pruebas E2E para ClienteController usando Mockito para simular entradas estáticas.
 */
class ClienteControllerE2ETest {
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
     * Prueba E2E para la creación de un cliente con entradas simuladas.
     */
    @Test
    @DisplayName("E2E: Creación de cliente exitosa")
    void testCrearClienteE2E() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Nombre completo"), eq(true))).thenReturn("Carlos Ruiz");
            mocked.when(() -> InputUtils.solicitarOpcion(any(Scanner.class), eq("Tipo de documento"), any(String[].class))).thenReturn("PASAPORTE");
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Número de documento"), eq(true))).thenReturn("789012");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), eq("Tipo de cliente"), eq(Cliente.TipoCliente.class))).thenReturn(Cliente.TipoCliente.EXTRANJERO);
            Cliente clienteMock = new Cliente();
            clienteMock.setCodigo("CLI-0003");
            clienteMock.setTipoCliente(Cliente.TipoCliente.EXTRANJERO);
            when(clienteService.crearCliente(anyString(), anyString(), anyString(), any())).thenReturn(clienteMock);
            controller.crearCliente(scanner);
            verify(clienteService).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }
}
