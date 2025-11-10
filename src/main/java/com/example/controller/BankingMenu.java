package com.example.controller;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankingMenu {
    private static final Logger logger = LoggerFactory.getLogger(BankingMenu.class);
    private final ClienteController clienteController;
    private final CuentaController cuentaController;
    private final CreditoController creditoController;
    private final TransaccionController transaccionController;

    public BankingMenu(ClienteController clienteController, CuentaController cuentaController,
            CreditoController creditoController, TransaccionController transaccionController) {
        this.clienteController = clienteController;
        this.cuentaController = cuentaController;
        this.creditoController = creditoController;
        this.transaccionController = transaccionController;
    }

    public void run() {
        run(new Scanner(System.in));
    }

    public void run(Scanner sc) {
        boolean running = true;
        while (running) {
            mostrarMenu();
            logger.info("\nOpción: ");
            String opcion = sc.nextLine().trim();
            switch (opcion) {
                case "1" -> clienteController.crearCliente(sc);
                case "2" -> clienteController.listarClientes();
                case "3" -> cuentaController.abrirCuenta(sc);
                case "4" -> cuentaController.listarCuentasCliente(sc);
                case "5" -> cuentaController.depositar(sc);
                case "6" -> cuentaController.retirar(sc);
                case "7" -> cuentaController.transferir(sc);
                case "8" -> cuentaController.consultarSaldo(sc);
                case "9" -> transaccionController.crearTransaccion(sc);
                case "10" -> creditoController.solicitarCredito(sc);
                case "11" -> creditoController.listarCreditosCliente(sc);
                case "12" -> creditoController.pagarCuotaCredito(sc);
                case "13" -> transaccionController.listarTransaccionesCuenta(sc);
                case "14" -> clienteController.cambiarScoreCliente(sc);
                case "15" -> cuentaController.consultarScoreCliente(sc);
                case "0" -> running = false;
                default -> logger.warn("Opción inválida");
            }
            if (running) {
                logger.info("\nPresione ENTER para continuar...");
                sc.nextLine();
            }
        }
        logger.info("\n¡Hasta pronto!");
        sc.close();
    }

    private void mostrarMenu() {
        logger.info("\n==============================");
        logger.info("MENÚ PRINCIPAL");
        logger.info("==============================");
        logger.info("1. Crear cliente");
        logger.info("2. Listar clientes");
        logger.info("3. Abrir cuenta");
        logger.info("4. Listar cuentas de cliente");
        logger.info("5. Depósito en cuenta");
        logger.info("6. Retiro de cuenta");
        logger.info("7. Transferencia entre cuentas");
        logger.info("8. Consultar saldo");
        logger.info("9. Crear transacción");
        logger.info("10. Solicitar crédito");
        logger.info("11. Listar créditos de cliente");
        logger.info("12. Pagar cuota de crédito");
        logger.info("13. Listar transacciones de cuenta");
        logger.info("14. Cambiar score de cliente");
        logger.info("15. Consultar score de cliente");
        logger.info("0. Salir");
        logger.info("==============================");
    }
}
