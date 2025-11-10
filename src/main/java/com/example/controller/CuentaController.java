package com.example.controller;

import com.example.adapter.buro.BuroFinancieroAdapter;
import com.example.adapter.legacy.LegacyRiskApiAdapter;
import com.example.adapter.score.ScoreProviderRegistry;
import com.example.repository.cliente.ClienteRepository;
import com.example.model.score.Score;
import com.example.model.cuenta.Cuenta;
import com.example.model.cliente.Cliente;
import com.example.service.cuenta.CuentaService;
import com.example.service.cliente.ClienteService;
import java.util.Optional;
import java.util.Scanner;

public class CuentaController {
    private final CuentaService cuentaService;
    private final ClienteService clienteService;
    private final ScoreProviderRegistry scoreProviderRegistry;

    public CuentaController(CuentaService cuentaService, ClienteService clienteService, ClienteRepository clienteRepository) {
        this.cuentaService = cuentaService;
        this.clienteService = clienteService;
        this.scoreProviderRegistry = new ScoreProviderRegistry(
            new BuroFinancieroAdapter(clienteRepository), new LegacyRiskApiAdapter()
        );
    }
    public void consultarScoreCliente(Scanner sc) {
        System.out.println("\nCONSULTA DE SCORE DEL CLIENTE\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            System.err.println("Cliente no encontrado");
            return;
        }
        Cliente cliente = clienteOpt.get();
        Score score = scoreProviderRegistry.obtenerScoreParaCliente(cliente);
        System.out.println("Score actual: " + score.getValor() + " (Fuente: " + score.getFuente() + ")");
    }

    // ...existing code...

    public void listarCuentasCliente(Scanner sc) {
        System.out.println("\nCUENTAS DEL CLIENTE\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            System.err.println("Cliente no encontrado");
            return;
        }
        Cliente cliente = clienteOpt.get();
        if (cliente.getCuentaIds().isEmpty()) {
            System.out.println("El cliente no tiene cuentas");
            return;
        }
        System.out.println("Cliente: " + cliente.getNombre() + "\n");
        System.out
                .println(String.format("%-12s %-20s %-12s %15s %-12s", "CÓDIGO", "NÚMERO", "TIPO", "SALDO", "ESTADO"));
        System.out.println("-".repeat(75));
        for (String cuentaId : cliente.getCuentaIds()) {
            cuentaService.obtenerCuenta(cuentaId)
                    .ifPresent(cuenta -> System.out.println(String.format("%-12s %-20s %-12s $%,14.2f %-12s",
                            cuenta.getCodigo(), cuenta.getNumeroCuenta(), cuenta.getTipoCuenta(), cuenta.getSaldo(),
                            cuenta.getEstado())));
        }
        System.out.println("-".repeat(75));
    }

    public void depositar(Scanner sc) {
        System.out.println("\nDEPÓSITO EN CUENTA\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            System.err.println("Cliente no encontrado");
            return;
        }
        Cliente cliente = clienteOpt.get();
        if (cliente.getCuentaIds().isEmpty()) {
            System.out.println("El cliente no tiene cuentas");
            return;
        }
        String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta", true);
        if (cuentaId == null)
            return;
        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuenta(cuentaId);
        if (cuentaOpt.isEmpty()) {
            System.err.println("Cuenta no encontrada");
            return;
        }
        Double monto = InputUtils.solicitarMonto(sc, "Monto a depositar");
        if (monto == null)
            return;
        try {
            cuentaService.depositar(cuentaId, monto);
            System.out.println("Depósito realizado correctamente");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void retirar(Scanner sc) {
        System.out.println("\nRETIRO DE CUENTA\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            System.err.println("Cliente no encontrado");
            return;
        }
        Cliente cliente = clienteOpt.get();
        if (cliente.getCuentaIds().isEmpty()) {
            System.out.println("El cliente no tiene cuentas");
            return;
        }
        String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta", true);
        if (cuentaId == null)
            return;
        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuenta(cuentaId);
        if (cuentaOpt.isEmpty()) {
            System.err.println("Cuenta no encontrada");
            return;
        }
        Double monto = InputUtils.solicitarMonto(sc, "Monto a retirar");
        if (monto == null)
            return;
        try {
            cuentaService.retirar(cuentaId, monto);
            System.out.println("Retiro realizado correctamente");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void transferir(Scanner sc) {
        System.out.println("\nTRANSFERENCIA ENTRE CUENTAS\n");
        String cuentaOrigen = InputUtils.solicitarTexto(sc, "Código de cuenta origen", true);
        if (cuentaOrigen == null)
            return;
        String cuentaDestino = InputUtils.solicitarTexto(sc, "Código de cuenta destino", true);
        if (cuentaDestino == null)
            return;
        Double monto = InputUtils.solicitarMonto(sc, "Monto a transferir");
        if (monto == null)
            return;
        try {
            cuentaService.transferir(cuentaOrigen, cuentaDestino, monto);
            System.out.println("Transferencia realizada correctamente");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void consultarSaldo(Scanner sc) {
        System.out.println("\nCONSULTA DE SALDO\n");
        String cuentaId = InputUtils.solicitarTexto(sc, "Código de cuenta", true);
        if (cuentaId == null)
            return;
        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuenta(cuentaId);
        if (cuentaOpt.isEmpty()) {
            System.err.println("Cuenta no encontrada");
            return;
        }
        Cuenta cuenta = cuentaOpt.get();
        System.out.println("Saldo actual: $" + String.format("%,.2f", cuenta.getSaldo()));
    }

    public void abrirCuenta(Scanner sc) {
        System.out.println("\nAPERTURA DE CUENTA\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, "Código de cliente", true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            System.err.println("Cliente no encontrado");
            return;
        }
        Cuenta.TipoCuenta tipo = InputUtils.solicitarEnum(sc, "Tipo de cuenta", Cuenta.TipoCuenta.class);
        if (tipo == null)
            return;
        Double saldoInicial = InputUtils.solicitarMonto(sc, "Saldo inicial");
        if (saldoInicial == null)
            return;
        try {
            Cuenta cuenta = cuentaService.abrirCuenta(clienteOpt.get().getId(), tipo, saldoInicial);
            System.out.println("\nCuenta creada exitosamente");
            System.out.println("Código: " + cuenta.getCodigo());
            System.out.println("Número: " + cuenta.getNumeroCuenta());
            System.out.println("Saldo: $" + String.format("%,.2f", cuenta.getSaldo()));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}
