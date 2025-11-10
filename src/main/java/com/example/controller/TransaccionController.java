package com.example.controller;

import com.example.service.transaccion.TransaccionService;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransaccionController {
    private static final String ERROR_MSG = "Error: {}";
    private static final String CODIGO_CUENTA_ORIGEN = "Código de cuenta origen";
    private static final Logger logger = LoggerFactory.getLogger(TransaccionController.class);
    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    public void crearTransaccion(Scanner sc) {
        logger.info("\nCREAR TRANSACCIÓN\n");
        logger.info("Tipo de transacción:");
        logger.info("1. Depósito");
        logger.info("2. Retiro");
        logger.info("3. Transferencia");
        logger.info("4. Pago de servicio");
        logger.info("0. Cancelar");
        logger.info("Opción: ");
        String opcion = sc.nextLine().trim();
        switch (opcion) {
            case "1" -> handleDeposito(sc);
            case "2" -> handleRetiro(sc);
            case "3" -> handleTransferencia(sc);
            case "4" -> handlePagoServicio(sc);
            case "0" -> logger.info("Operación cancelada");
            default -> logger.error("Opción inválida");
        }
    }

    private void handleDeposito(Scanner sc) {
        String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta destino", true);
        if (cuentaId == null)
            return;
        Double monto = InputUtils.solicitarMonto(sc, "Monto a depositar");
        if (monto == null)
            return;
        String descripcion = InputUtils.solicitarTexto(sc, "Descripción (opcional)", false);
        try {
            transaccionService.crearDeposito(cuentaId, monto, descripcion);
            logger.info("Depósito realizado correctamente");
        } catch (Exception e) {
            logger.error(ERROR_MSG, e.getMessage());
        }
    }

    private void handleRetiro(Scanner sc) {
        String cuentaId = InputUtils.solicitarTexto(sc, CODIGO_CUENTA_ORIGEN, true);
        if (cuentaId == null)
            return;
        Double monto = InputUtils.solicitarMonto(sc, "Monto a retirar");
        if (monto == null)
            return;
        String ubicacion = InputUtils.solicitarTexto(sc, "Ubicación cajero/sucursal (opcional)", false);
        try {
            transaccionService.crearRetiro(cuentaId, monto, ubicacion);
            logger.info("Retiro realizado correctamente");
        } catch (Exception e) {
            logger.error(ERROR_MSG, e.getMessage());
        }
    }

    private void handleTransferencia(Scanner sc) {
        String cuentaOrigen = InputUtils.solicitarTexto(sc, CODIGO_CUENTA_ORIGEN, true);
        if (cuentaOrigen == null)
            return;
        String cuentaDestino = InputUtils.solicitarTexto(sc, "Código de cuenta destino", true);
        if (cuentaDestino == null)
            return;
        Double monto = InputUtils.solicitarMonto(sc, "Monto a transferir");
        if (monto == null)
            return;
        String descripcion = InputUtils.solicitarTexto(sc, "Descripción (opcional)", false);
        try {
            transaccionService.crearTransferencia(cuentaOrigen, cuentaDestino, monto, descripcion);
            logger.info("Transferencia realizada correctamente");
        } catch (Exception e) {
            logger.error(ERROR_MSG, e.getMessage());
        }
    }

    private void handlePagoServicio(Scanner sc) {
        String cuentaId = InputUtils.solicitarTexto(sc, CODIGO_CUENTA_ORIGEN, true);
        if (cuentaId == null)
            return;
        String codigoServicio = InputUtils.solicitarTexto(sc, "Código de servicio", true);
        if (codigoServicio == null)
            return;
        Double monto = InputUtils.solicitarMonto(sc, "Monto a pagar");
        if (monto == null)
            return;
        String referencia = InputUtils.solicitarTexto(sc, "Referencia de pago", true);
        if (referencia == null)
            return;
        try {
            transaccionService.crearPagoServicio(cuentaId, codigoServicio, monto, referencia);
            logger.info("Pago realizado correctamente");
        } catch (Exception e) {
            logger.error(ERROR_MSG, e.getMessage());
        }
    }

    public void listarTransaccionesCuenta(Scanner sc) {
        logger.info("\nMOVIMIENTOS/TRANSACCIONES DE CUENTA\n");
        String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta", true);
        if (cuentaId == null)
            return;
        var transacciones = transaccionService.listarTransaccionesCuenta(cuentaId);
        if (transacciones.isEmpty()) {
            logger.info("No hay transacciones para esta cuenta");
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("%-12s %-12s %-10s %-12s %-12s", "CÓDIGO", "FECHA", "TIPO", "MONTO", "DETALLE"));
            logger.info("-".repeat(60));
            for (var t : transacciones) {
                logger.info(String.format("%-12s %-12s %-10s $%,10.2f %-12s", t.getCodigo(), t.getFecha(),
                        t.getTipo(), t.getMonto(), t.getDescripcion()));
            }
            logger.info("-".repeat(60));
        }
    }
}
