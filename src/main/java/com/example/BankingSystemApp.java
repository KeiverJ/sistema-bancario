
package com.example;

public class BankingSystemApp {
  public static void main(String[] args) {
    BankingSystemContext context = new BankingSystemContext();

    // Menú principal
    com.example.controller.BankingMenu menu = new com.example.controller.BankingMenu(
        context.clienteController,
        context.cuentaController,
        context.creditoController,
        context.transaccionController);
    menu.run();
  }
}