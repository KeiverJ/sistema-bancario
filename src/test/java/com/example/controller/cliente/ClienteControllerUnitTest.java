
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Pruebas para ClienteController
 *
 * --- UNIT TESTS ---
 * Pruebas unitarias usando Mockito para simular entradas estáticas.
 *
 * --- INTEGRATION TESTS ---
 * Aquí irán pruebas de integración con dependencias reales o semi-mockeadas.
 *
 * --- E2E TESTS ---
 * Aquí irán pruebas de flujo completo de cliente (alta, consulta, etc).
 */
class ClienteControllerUnitTest {
    // --- UNIT TESTS ---

    @Test
    @DisplayName("Unit: listarClientes muestra clientes")
    void testListarClientesConClientes() {
        Cliente c1 = new Cliente();
        c1.setCodigo("CLI-1");
        c1.setNombre("Juan Perez");
        c1.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        c1.setNumeroDocumento("123");
        when(clienteService.listarTodos()).thenReturn(java.util.List.of(c1));
        controller.listarClientes();
        verify(clienteService).listarTodos();
    }

    @Test
    @DisplayName("Unit: listarClientes sin clientes")
    void testListarClientesSinClientes() {
        when(clienteService.listarTodos()).thenReturn(java.util.List.of());
        controller.listarClientes();
        verify(clienteService).listarTodos();
    }

    @Test
    @DisplayName("Unit: seleccionarCliente retorna código válido")
    void testSeleccionarClienteExito() {
        Cliente c1 = new Cliente();
        c1.setCodigo("CLI-1");
        c1.setNombre("Juan Perez");
        when(clienteService.listarTodos()).thenReturn(java.util.List.of(c1));
        when(scanner.nextLine()).thenReturn("CLI-1");
        when(clienteService.obtenerClientePorCodigo("CLI-1")).thenReturn(java.util.Optional.of(c1));
        String codigo = controller.seleccionarCliente(scanner);
        assertEquals("CLI-1", codigo);
    }

    @Test
    @DisplayName("Unit: seleccionarCliente retorna null si no hay clientes")
    void testSeleccionarClienteSinClientes() {
        when(clienteService.listarTodos()).thenReturn(java.util.List.of());
        String codigo = controller.seleccionarCliente(scanner);
        assertNull(codigo);
    }

    @Test
    @DisplayName("Unit: seleccionarCliente retorna null si código no existe")
    void testSeleccionarClienteCodigoNoExiste() {
        Cliente c1 = new Cliente();
        c1.setCodigo("CLI-1");
        when(clienteService.listarTodos()).thenReturn(java.util.List.of(c1));
        when(scanner.nextLine()).thenReturn("CLI-2");
        when(clienteService.obtenerClientePorCodigo("CLI-2")).thenReturn(java.util.Optional.empty());
        String codigo = controller.seleccionarCliente(scanner);
        assertNull(codigo);
    }

    @Test
    @DisplayName("Unit: cambiarScoreCliente ejecuta flujo feliz")
    void testCambiarScoreClienteExito() {
        Cliente c1 = new Cliente();
        c1.setCodigo("CLI-1");
        c1.setNombre("Juan Perez");
        when(clienteService.listarTodos()).thenReturn(java.util.List.of(c1));
        when(scanner.nextLine()).thenReturn("CLI-1", "100");
        when(clienteService.obtenerClientePorCodigo("CLI-1")).thenReturn(java.util.Optional.of(c1));
        controller.cambiarScoreCliente(scanner);
        verify(clienteService).obtenerClientePorCodigo("CLI-1");
    }

    @Test
    @DisplayName("Unit: cambiarScoreCliente sin clientes (usuario cancela)")
    void testCambiarScoreClienteSinClientes() {
        // Simula que el usuario cancela la operación devolviendo "0"
        when(scanner.nextLine()).thenReturn("0");
        controller.cambiarScoreCliente(scanner);
        // No debe haber interacción con el servicio porque el usuario cancela antes de consultar
        verifyNoInteractions(clienteService);
    }
    /**
     * Prueba: documento duplicado al crear cliente.
     */
    @Test
    @DisplayName("Unit: Documento duplicado en crearCliente")
    void testCrearClienteDocumentoDuplicado() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn("Ana");
            mocked.when(() -> InputUtils.solicitarOpcion(any(), eq("Tipo de documento"), any(String[].class))).thenReturn("CC");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Número de documento"), eq(true))).thenReturn("123");
            mocked.when(() -> InputUtils.solicitarEnum(any(), eq("Tipo de cliente"), eq(Cliente.TipoCliente.class))).thenReturn(Cliente.TipoCliente.PERSONA_NATURAL);
            when(clienteService.crearCliente(anyString(), anyString(), anyString(), any())).thenThrow(new IllegalArgumentException("Documento ya existe"));
            controller.crearCliente(scanner);
            verify(clienteService).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba: excepción inesperada del servicio al crear cliente.
     */
    @Test
    @DisplayName("Unit: Excepción inesperada en crearCliente")
    void testCrearClienteExcepcionServicio() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn("Ana");
            mocked.when(() -> InputUtils.solicitarOpcion(any(), eq("Tipo de documento"), any(String[].class))).thenReturn("CC");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Número de documento"), eq(true))).thenReturn("123");
            mocked.when(() -> InputUtils.solicitarEnum(any(), eq("Tipo de cliente"), eq(Cliente.TipoCliente.class))).thenReturn(Cliente.TipoCliente.PERSONA_NATURAL);
            when(clienteService.crearCliente(anyString(), anyString(), anyString(), any())).thenThrow(new RuntimeException("Error inesperado"));
            controller.crearCliente(scanner);
            verify(clienteService).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba: nombre vacío al crear cliente.
     */
    @Test
    @DisplayName("Unit: Nombre vacío en crearCliente")
    void testCrearClienteNombreVacio() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn("");
            controller.crearCliente(scanner);
            verify(clienteService, never()).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba: documento inválido al crear cliente.
     */
    @Test
    @DisplayName("Unit: Documento inválido en crearCliente")
    void testCrearClienteDocumentoInvalido() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn("Ana");
            mocked.when(() -> InputUtils.solicitarOpcion(any(), eq("Tipo de documento"), any(String[].class))).thenReturn("CC");
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Número de documento"), eq(true))).thenReturn("");
            controller.crearCliente(scanner);
            verify(clienteService, never()).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba: creación de cliente tipo empresa.
     */
    @Test
    @DisplayName("Unit: Creación de cliente persona jurídica")
    void testCrearClientePersonaJuridica() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Nombre completo"), eq(true))).thenReturn("Empresa XYZ");
            mocked.when(() -> InputUtils.solicitarOpcion(any(Scanner.class), eq("Tipo de documento"), any(String[].class))).thenReturn("NIT");
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Número de documento"), eq(true))).thenReturn("900123456");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), eq("Tipo de cliente"), eq(Cliente.TipoCliente.class))).thenReturn(Cliente.TipoCliente.PERSONA_JURIDICA);
            Cliente clienteMock = new Cliente();
            clienteMock.setCodigo("CLI-0002");
            clienteMock.setTipoCliente(Cliente.TipoCliente.PERSONA_JURIDICA);
            when(clienteService.crearCliente(anyString(), anyString(), anyString(), any())).thenReturn(clienteMock);
            controller.crearCliente(scanner);
            verify(clienteService).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }
    /**
     * Prueba: nombre nulo al crear cliente.
     */
    @Test
    @DisplayName("Unit: Nombre nulo en crearCliente")
    void testCrearClienteNombreNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn(null);
            controller.crearCliente(scanner);
            verify(clienteService, never()).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba: tipo de documento nulo al crear cliente.
     */
    @Test
    @DisplayName("Unit: Tipo de documento nulo en crearCliente")
    void testCrearClienteTipoDocNulo() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(), eq("Nombre completo"), eq(true))).thenReturn("Ana");
            mocked.when(() -> InputUtils.solicitarOpcion(any(), eq("Tipo de documento"), any(String[].class))).thenReturn(null);
            controller.crearCliente(scanner);
            verify(clienteService, never()).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }

    /**
     * Prueba: número de documento nulo al crear cliente.
     */
    @Test
    @DisplayName("Unit: Número de documento nulo en crearCliente")
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
     * Prueba: tipo de cliente nulo al crear cliente.
     */
    @Test
    @DisplayName("Unit: Tipo de cliente nulo en crearCliente")
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
     * Prueba unitaria para la creación de un cliente usando entradas simuladas.
     */
    @Test
    @DisplayName("Unit: Creación de cliente exitosa")
    void testCrearCliente() {
        try (var mocked = mockStatic(InputUtils.class)) {
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Nombre completo"), eq(true)))
                    .thenReturn("Juan Perez");
            mocked.when(
                    () -> InputUtils.solicitarOpcion(any(Scanner.class), eq("Tipo de documento"), any(String[].class)))
                    .thenReturn("CC");
            mocked.when(() -> InputUtils.solicitarTexto(any(Scanner.class), eq("Número de documento"), eq(true)))
                    .thenReturn("123456");
            mocked.when(() -> InputUtils.solicitarEnum(any(Scanner.class), eq("Tipo de cliente"),
                    eq(Cliente.TipoCliente.class))).thenReturn(Cliente.TipoCliente.PERSONA_NATURAL);
            Cliente clienteMock = new Cliente();
            clienteMock.setCodigo("CLI-0001");
            clienteMock.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
            when(clienteService.crearCliente(anyString(), anyString(), anyString(), any())).thenReturn(clienteMock);
            controller.crearCliente(scanner);
            verify(clienteService).crearCliente(anyString(), anyString(), anyString(), any());
        }
    }
}
