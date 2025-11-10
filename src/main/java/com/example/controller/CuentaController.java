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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CuentaController {
    private static final Logger logger = LoggerFactory.getLogger(CuentaController.class);
    private static final String CODIGO_CLIENTE_LABEL = "Código de cliente";
    private static final String CLIENTE_NO_ENCONTRADO_MSG = "Cliente no encontrado";
    private static final String CLIENTE_SIN_CUENTAS_MSG = "El cliente no tiene cuentas";
    private static final String CUENTA_NO_ENCONTRADA_MSG = "Cuenta no encontrada";
    private static final String CODIGO_CUENTA_LABEL = "Código de cuenta";
    private static final String ERROR_MSG = "Error: {}";
    private final CuentaService cuentaService;
    private final ClienteService clienteService;
    private final ScoreProviderRegistry scoreProviderRegistry;

    public CuentaController(CuentaService cuentaService, ClienteService clienteService,
            ClienteRepository clienteRepository) {
        this.cuentaService = cuentaService;
        this.clienteService = clienteService;
        this.scoreProviderRegistry = new ScoreProviderRegistry(
                new BuroFinancieroAdapter(clienteRepository), new LegacyRiskApiAdapter());
    }

    public void consultarScoreCliente(Scanner sc) {
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, CODIGO_CLIENTE_LABEL, true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            logger.error(CLIENTE_NO_ENCONTRADO_MSG);
            return;
        }
        Cliente cliente = clienteOpt.get();
        Score score = scoreProviderRegistry.obtenerScoreParaCliente(cliente);
        logger.info("Score actual: {} (Fuente: {})", score.getValor(), score.getFuente());
    }

    public void listarCuentasCliente(Scanner sc) {
        logger.info("\nCUENTAS DEL CLIENTE\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, CODIGO_CLIENTE_LABEL, true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            logger.error(CLIENTE_NO_ENCONTRADO_MSG);
            return;
        }
        Cliente cliente = clienteOpt.get();
        if (cliente.getCuentaIds().isEmpty()) {
            logger.info(CLIENTE_SIN_CUENTAS_MSG);
            return;
        }

        logger.info("Cliente: {}\n", cliente.getNombre());
        if (logger.isInfoEnabled()) {
            logger.info(String.format("%-12s %-20s %-12s %15s %-12s", "CÓDIGO", "NÚMERO", "TIPO", "SALDO", "ESTADO"));
            logger.info("-".repeat(75));
        }
        for (String cuentaId : cliente.getCuentaIds()) {
            cuentaService.obtenerCuenta(cuentaId)
                    .ifPresent(cuenta -> logger.info(String.format("%-12s %-20s %-12s $%,14.2f %-12s",
                            cuenta.getCodigo(), cuenta.getNumeroCuenta(), cuenta.getTipoCuenta(), cuenta.getSaldo(),
                            cuenta.getEstado())));
        }
        if (logger.isInfoEnabled()) {
            logger.info("-".repeat(75));
        }
    }

    public void depositar(Scanner sc) {
        logger.info("\nDEPÓSITO EN CUENTA\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, CODIGO_CLIENTE_LABEL, true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            logger.error(CLIENTE_NO_ENCONTRADO_MSG);
            return;
        }
        Cliente cliente = clienteOpt.get();
        if (cliente.getCuentaIds().isEmpty()) {
            logger.info(CLIENTE_SIN_CUENTAS_MSG);
            return;
        }
        String cuentaId = InputUtils.solicitarTexto(sc, CODIGO_CUENTA_LABEL, true);
        if (cuentaId == null)
            return;
        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuenta(cuentaId);
        if (cuentaOpt.isEmpty()) {
            logger.error(CUENTA_NO_ENCONTRADA_MSG);
            return;
        }
        Double monto = InputUtils.solicitarMonto(sc, "Monto a depositar");
        if (monto == null)
            return;
        try {
            cuentaService.depositar(cuentaId, monto);
            logger.info("Depósito realizado correctamente");
        } catch (Exception e) {
            logger.error(ERROR_MSG, e.getMessage());
        }
    }

    public void retirar(Scanner sc) {
        logger.info("\nRETIRO DE CUENTA\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, CODIGO_CLIENTE_LABEL, true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            logger.error(CLIENTE_NO_ENCONTRADO_MSG);
            return;
        }
        Cliente cliente = clienteOpt.get();
        if (cliente.getCuentaIds().isEmpty()) {
            logger.info(CLIENTE_SIN_CUENTAS_MSG);
            return;
        }
        String cuentaId = InputUtils.solicitarTexto(sc, CODIGO_CUENTA_LABEL, true);
        if (cuentaId == null)
            return;
        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuenta(cuentaId);
        if (cuentaOpt.isEmpty()) {
            logger.error(CUENTA_NO_ENCONTRADA_MSG);
            return;
        }
        Double monto = InputUtils.solicitarMonto(sc, "Monto a retirar");
        if (monto == null)
            return;
        try {
            cuentaService.retirar(cuentaId, monto);
            logger.info("Retiro realizado correctamente");
        } catch (Exception e) {
            logger.error(ERROR_MSG, e.getMessage());
        }
    }

    public void transferir(Scanner sc) {
        logger.info("\nTRANSFERENCIA ENTRE CUENTAS\n");
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
            logger.info("Transferencia realizada correctamente");
        } catch (Exception e) {
            logger.error(ERROR_MSG, e.getMessage());
        }
    }

    public void consultarSaldo(Scanner sc) {
        String cuentaId = InputUtils.solicitarTexto(sc, CODIGO_CUENTA_LABEL, true);
        if (cuentaId == null)
            return;
        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuenta(cuentaId);
        if (cuentaOpt.isEmpty()) {
            logger.error(CUENTA_NO_ENCONTRADA_MSG);
            return;
        }
        Cuenta cuenta = cuentaOpt.get();
        if (logger.isInfoEnabled()) {
            logger.info("Saldo actual: ${}", String.format("%,.2f", cuenta.getSaldo()));
        }
    }

    public void abrirCuenta(Scanner sc) {
        logger.info("\nAPERTURA DE CUENTA\n");
        String codigoCliente = clienteService.listarTodos().isEmpty() ? null
                : InputUtils.solicitarTexto(sc, CODIGO_CLIENTE_LABEL, true);
        if (codigoCliente == null)
            return;
        Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
        if (clienteOpt.isEmpty()) {
            logger.error(CLIENTE_NO_ENCONTRADO_MSG);
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
            logger.info("\nCuenta creada exitosamente");
            logger.info("Código: {}", cuenta.getCodigo());
            logger.info("Número: {}", cuenta.getNumeroCuenta());
            if (logger.isInfoEnabled()) {
                logger.info("Saldo: ${}", String.format("%,.2f", cuenta.getSaldo()));
            }
        } catch (Exception e) {
            logger.error(ERROR_MSG, e.getMessage());
        }

    }
}
