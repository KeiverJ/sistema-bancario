package com.example.controller;

import com.example.model.credito.Credito;
import com.example.model.cliente.Cliente;
import com.example.service.credito.CreditoService;
import com.example.service.cliente.ClienteService;
import java.util.Optional;
import java.util.Scanner;

public class CreditoController {
    private final CreditoService creditoService;
    private final ClienteService clienteService;

    public CreditoController(CreditoService creditoService, ClienteService clienteService) {
        this.creditoService = creditoService;
        this.clienteService = clienteService;
    }

    public void solicitarCredito(Scanner sc) {
        System.out.println("\nSOLICITUD DE CRÉDITO\n");
        String codigo = clienteService.listarTodos().isEmpty() ? null : InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigo == null) return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigo);
        if (clienteOpt.isEmpty()) {
            System.err.println("Cliente no encontrado");
            return;
        }
        Credito.TipoCredito tipo = InputUtils.solicitarEnum(sc, "Tipo de crédito", Credito.TipoCredito.class);
        if (tipo == null) return;
        Double monto = InputUtils.solicitarMonto(sc, "Monto solicitado");
        if (monto == null) return;
        Integer plazo = InputUtils.solicitarEnteroPositivo(sc, "Plazo en meses");
        if (plazo == null) return;
        try {
            Credito cr = creditoService.solicitarCredito(clienteOpt.get().getId(), tipo, monto, plazo);
            System.out.println("\nCrédito solicitado");
            System.out.println("Código: " + cr.getCodigo());
            System.out.println("Monto: $" + String.format("%,.2f", cr.getMonto()));
            System.out.println("Tasa: " + cr.getTasaInteres() + "%");
            System.out.println("Estado: " + cr.getEstadoActual());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void listarCreditosCliente(Scanner sc) {
        System.out.println("\nCRÉDITOS DEL CLIENTE\n");
        String codigo = clienteService.listarTodos().isEmpty() ? null : InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigo == null) return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigo);
        if (clienteOpt.isEmpty()) {
            System.err.println("Cliente no encontrado");
            return;
        }
        Cliente cliente = clienteOpt.get();
        var creditos = creditoService.listarCreditosCliente(cliente.getId());
        if (creditos.isEmpty()) {
            System.out.println("El cliente no tiene créditos");
            return;
        }
        System.out.println(String.format("%-12s %-10s %-12s %-12s %-10s %-10s", "CÓDIGO", "TIPO", "MONTO", "SALDO", "TASA", "ESTADO"));
        System.out.println("-".repeat(70));
        for (Credito cr : creditos) {
            System.out.println(String.format("%-12s %-10s $%,10.2f $%,10.2f %-10.2f %-10s", cr.getCodigo(), cr.getTipoCredito(), cr.getMonto(), cr.getSaldo(), cr.getTasaInteres(), cr.getEstadoActual()));
        }
        System.out.println("-".repeat(70));
    }

    public void pagarCuotaCredito(Scanner sc) {
        System.out.println("\nPAGO DE CUOTA DE CRÉDITO\n");
        String codigoCredito = InputUtils.solicitarTexto(sc, "Código de crédito", true);
        if (codigoCredito == null) return;
        Optional<Credito> crOpt = creditoService.obtenerCreditoPorCodigo(codigoCredito);
        if (crOpt.isEmpty()) {
            System.err.println("Crédito no encontrado");
            return;
        }
        Credito cr = crOpt.get();
        System.out.println("Saldo actual: $" + String.format("%,.2f", cr.getSaldo()));
        Double monto = InputUtils.solicitarMonto(sc, "Monto a pagar");
        if (monto == null) return;
        try {
            creditoService.pagarCuota(codigoCredito, monto);
            System.out.println("Pago realizado correctamente");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
