package com.example.controller;

import com.example.service.cliente.ClienteService;
import java.util.Scanner;

public class BankingMenu {
    private final ClienteController clienteController;
    private final CuentaController cuentaController;
    private final CreditoController creditoController;
    private final TransaccionController transaccionController;

    public BankingMenu(ClienteController clienteController, CuentaController cuentaController, CreditoController creditoController, TransaccionController transaccionController) {
        this.clienteController = clienteController;
        this.cuentaController = cuentaController;
        this.creditoController = creditoController;
        this.transaccionController = transaccionController;
    }

    public void run() {
        run(new Scanner(System.in));
    }

    // Permite inyectar Scanner externo para pruebas
    public void run(Scanner sc) {
        boolean running = true;
        while (running) {
            mostrarMenu();
            System.out.print("\nOpción: ");
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
                default -> System.out.println("Opción inválida");
            }
            if (running) {
                System.out.println("\nPresione ENTER para continuar...");
                sc.nextLine();
            }
        }
        System.out.println("\n¡Hasta pronto!");
        sc.close();
    }

    private void mostrarMenu() {
        System.out.println("\n==============================");
        System.out.println("MENÚ PRINCIPAL");
        System.out.println("==============================");
        System.out.println("1. Crear cliente");
        System.out.println("2. Listar clientes");
        System.out.println("3. Abrir cuenta");
        System.out.println("4. Listar cuentas de cliente");
        System.out.println("5. Depósito en cuenta");
        System.out.println("6. Retiro de cuenta");
        System.out.println("7. Transferencia entre cuentas");
        System.out.println("8. Consultar saldo");
        System.out.println("9. Crear transacción");
        System.out.println("10. Solicitar crédito");
        System.out.println("11. Listar créditos de cliente");
        System.out.println("12. Pagar cuota de crédito");
        System.out.println("13. Listar transacciones de cuenta");
    System.out.println("14. Cambiar score de cliente");
    System.out.println("15. Consultar score de cliente");
        System.out.println("0. Salir");
        System.out.println("==============================");
    }
}
