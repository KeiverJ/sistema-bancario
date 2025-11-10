package com.example.controller;

import com.example.service.transaccion.TransaccionService;
import com.example.service.cuenta.CuentaService;
import java.util.Scanner;

public class TransaccionController {
    private final TransaccionService transaccionService;
    private final CuentaService cuentaService;

    public TransaccionController(TransaccionService transaccionService, CuentaService cuentaService) {
        this.transaccionService = transaccionService;
        this.cuentaService = cuentaService;
    }

    public void crearTransaccion(Scanner sc) {
        System.out.println("\nCREAR TRANSACCIÓN\n");
        System.out.println("Tipo de transacción:");
        System.out.println("1. Depósito");
        System.out.println("2. Retiro");
        System.out.println("3. Transferencia");
        System.out.println("4. Pago de servicio");
        System.out.println("0. Cancelar");
        System.out.print("Opción: ");
        String opcion = sc.nextLine().trim();
        switch (opcion) {
            case "1" -> {
                String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta destino", true);
                if (cuentaId == null)
                    return;
                Double monto = InputUtils.solicitarMonto(sc, "Monto a depositar");
                if (monto == null)
                    return;
                String descripcion = InputUtils.solicitarTexto(sc, "Descripción (opcional)", false);
                try {
                    transaccionService.crearDeposito(cuentaId, monto, descripcion);
                    System.out.println("Depósito realizado correctamente");
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
            case "2" -> {
                String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta origen", true);
                if (cuentaId == null)
                    return;
                Double monto = InputUtils.solicitarMonto(sc, "Monto a retirar");
                if (monto == null)
                    return;
                String ubicacion = InputUtils.solicitarTexto(sc, "Ubicación cajero/sucursal (opcional)", false);
                try {
                    transaccionService.crearRetiro(cuentaId, monto, ubicacion);
                    System.out.println("Retiro realizado correctamente");
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
            case "3" -> {
                String cuentaOrigen = InputUtils.solicitarTexto(sc, "Código de cuenta origen", true);
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
                    System.out.println("Transferencia realizada correctamente");
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
            case "4" -> {
                String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta origen", true);
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
                    System.out.println("Pago realizado correctamente");
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
            case "0" -> {
                System.out.println("Operación cancelada");
                return;
            }
            default -> System.out.println("Opción inválida");
        }
    }

    public void listarTransaccionesCuenta(Scanner sc) {
        System.out.println("\nMOVIMIENTOS/TRANSACCIONES DE CUENTA\n");
        String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta", true);
        if (cuentaId == null)
            return;
        var transacciones = transaccionService.listarTransaccionesCuenta(cuentaId);
        if (transacciones.isEmpty()) {
            System.out.println("No hay transacciones para esta cuenta");
            return;
        }
        System.out
                .println(String.format("%-12s %-12s %-10s %-12s %-12s", "CÓDIGO", "FECHA", "TIPO", "MONTO", "DETALLE"));
        System.out.println("-".repeat(60));
        for (var t : transacciones) {
            System.out.println(String.format("%-12s %-12s %-10s $%,10.2f %-12s", t.getCodigo(), t.getFecha(),
                    t.getTipo(), t.getMonto(), t.getDescripcion()));
        }
        System.out.println("-".repeat(60));
    }
}
