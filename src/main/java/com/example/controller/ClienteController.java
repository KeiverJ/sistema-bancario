package com.example.controller;

import com.example.model.cliente.Cliente;
import com.example.service.cliente.ClienteService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClienteController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void crearCliente(Scanner sc) {
    logger.info("\nCREAR NUEVO CLIENTE\n");
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
            logger.info("\nCliente creado exitosamente");
            logger.info("Código: {}", cliente.getCodigo());
            logger.info("Tipo: {}", cliente.getTipoCliente());
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
        }
    }

    public void listarClientes() {
    logger.info("\nLISTADO DE CLIENTES\n");
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes.isEmpty()) {
            logger.info("No hay clientes registrados");
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("%-12s %-30s %-20s %-15s", "CÓDIGO", "NOMBRE", "TIPO", "DOCUMENTO"));
            logger.info("-".repeat(80));
            clientes.forEach(c -> logger.info(String.format("%-12s %-30s %-20s %-15s", c.getCodigo(), truncar(c.getNombre(), 30), c.getTipoCliente(), c.getNumeroDocumento())));
            logger.info("-".repeat(80));
        }
        logger.info("Total: {} cliente(s)", clientes.size());
    }

    public String seleccionarCliente(Scanner sc) {
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes.isEmpty()) {
            logger.error("No hay clientes. Cree un cliente primero");
            return null;
        }
        logger.info("\nClientes:");
        if (logger.isInfoEnabled()) {
            logger.info(String.format("%-12s %-30s", "CÓDIGO", "NOMBRE"));
            logger.info("-".repeat(45));
            clientes.forEach(c -> logger.info(String.format("%-12s %-30s", c.getCodigo(), truncar(c.getNombre(), 30))));
            logger.info("-".repeat(45));
        }
        String codigo = InputUtils.solicitarTexto(sc, "\nCódigo del cliente", true);
        if (codigo != null && clienteService.obtenerClientePorCodigo(codigo).isEmpty()) {
            logger.error("Cliente no encontrado");
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
    logger.info("\nCAMBIAR SCORE DE CLIENTE\n");
        String codigo = InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigo == null) return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigo);
        if (clienteOpt.isEmpty()) {
            logger.error("Cliente no encontrado");
            return;
        }
        Integer nuevoScore = InputUtils.solicitarEnteroPositivo(sc, "Nuevo score (300-850)");
        if (nuevoScore == null) return;
        try {
            Cliente actualizado = clienteService.actualizarScorePorCodigo(codigo, nuevoScore);
            logger.info("Score actualizado correctamente. Nuevo score: {}", actualizado.getScoreActual());
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
        }
    }
}
