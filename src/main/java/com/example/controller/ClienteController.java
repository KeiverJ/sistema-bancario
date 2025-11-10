package com.example.controller;

import com.example.model.cliente.Cliente;
import com.example.service.cliente.ClienteService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void crearCliente(Scanner sc) {
        System.out.println("\nCREAR NUEVO CLIENTE\n");
        String nombre = InputUtils.solicitarTexto(sc, "Nombre completo", true);
        if (nombre == null) return;
        String tipoDoc = InputUtils.solicitarOpcion(sc, "Tipo de documento", new String[]{"CC", "NIT", "PASAPORTE"});
        if (tipoDoc == null) return;
        String numDoc = InputUtils.solicitarTexto(sc, "Número de documento", true);
        if (numDoc == null) return;
        Cliente.TipoCliente tipoCliente = InputUtils.solicitarEnum(sc, "Tipo de cliente", Cliente.TipoCliente.class);
        if (tipoCliente == null) return;
        try {
            Cliente cliente = clienteService.crearCliente(nombre, tipoDoc, numDoc, tipoCliente);
            System.out.println("\nCliente creado exitosamente");
            System.out.println("Código: " + cliente.getCodigo());
            System.out.println("Tipo: " + cliente.getTipoCliente());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void listarClientes() {
        System.out.println("\nLISTADO DE CLIENTES\n");
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
            return;
        }
        System.out.println(String.format("%-12s %-30s %-20s %-15s", "CÓDIGO", "NOMBRE", "TIPO", "DOCUMENTO"));
        System.out.println("-".repeat(80));
        clientes.forEach(c -> System.out.println(
            String.format("%-12s %-30s %-20s %-15s", c.getCodigo(), truncar(c.getNombre(), 30), c.getTipoCliente(), c.getNumeroDocumento())));
        System.out.println("-".repeat(80));
        System.out.println("Total: " + clientes.size() + " cliente(s)");
    }

    public String seleccionarCliente(Scanner sc) {
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes.isEmpty()) {
            System.err.println("No hay clientes. Cree un cliente primero");
            return null;
        }
        System.out.println("\nClientes:");
        System.out.println(String.format("%-12s %-30s", "CÓDIGO", "NOMBRE"));
        System.out.println("-".repeat(45));
        clientes.forEach(c -> System.out.println(
            String.format("%-12s %-30s", c.getCodigo(), truncar(c.getNombre(), 30))));
        System.out.println("-".repeat(45));
        String codigo = InputUtils.solicitarTexto(sc, "\nCódigo del cliente", true);
        if (codigo != null && clienteService.obtenerClientePorCodigo(codigo).isEmpty()) {
            System.err.println("Cliente no encontrado");
            return null;
        }
        return codigo;
    }

    public Optional<Cliente> obtenerClientePorCodigo(String codigo) {
        return clienteService.obtenerClientePorCodigo(codigo);
    }

    private String truncar(String texto, int longitud) {
        if (texto == null) return "";
        return texto.length() > longitud ? texto.substring(0, longitud - 3) + "..." : texto;
    }

    public void cambiarScoreCliente(Scanner sc) {
        System.out.println("\nCAMBIAR SCORE DE CLIENTE\n");
        String codigo = InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigo == null) return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigo);
        if (clienteOpt.isEmpty()) {
            System.err.println("Cliente no encontrado");
            return;
        }
        Integer nuevoScore = InputUtils.solicitarEnteroPositivo(sc, "Nuevo score (300-850)");
        if (nuevoScore == null) return;
        try {
            Cliente actualizado = clienteService.actualizarScorePorCodigo(codigo, nuevoScore);
            System.out.println("Score actualizado correctamente. Nuevo score: " + actualizado.getScoreActual());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
