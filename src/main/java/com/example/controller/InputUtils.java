package com.example.controller;

import java.util.Scanner;

public class InputUtils {
    private static final String ABORTAR = "0";

    public static String solicitarTexto(Scanner sc, String mensaje, boolean obligatorio) {
        while (true) {
            System.out.print(mensaje + (obligatorio ? "" : " (opcional)") + " [0=cancelar]: ");
            String valor = sc.nextLine().trim();
            if (valor.equals(ABORTAR)) {
                System.out.println("Operación cancelada");
                return null;
            }
            if (!valor.isEmpty() || !obligatorio) {
                return valor;
            }
            System.err.println("Campo obligatorio");
        }
    }

    public static Double solicitarMonto(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje + " [0=cancelar]: $");
            String entrada = sc.nextLine().trim();
            if (entrada.equals(ABORTAR)) {
                System.out.println("Operación cancelada");
                return null;
            }
            try {
                double monto = Double.parseDouble(entrada);
                if (monto > 0) {
                    return monto;
                }
                System.err.println("Debe ser mayor a cero");
            } catch (NumberFormatException e) {
                System.err.println("Monto inválido");
            }
        }
    }

    public static Integer solicitarEnteroPositivo(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje + " [0=cancelar]: ");
            String entrada = sc.nextLine().trim();
            if (entrada.equals(ABORTAR)) {
                System.out.println("Operación cancelada");
                return null;
            }
            try {
                int valor = Integer.parseInt(entrada);
                if (valor > 0) {
                    return valor;
                }
                System.err.println("Debe ser mayor a cero");
            } catch (NumberFormatException e) {
                System.err.println("Valor inválido");
            }
        }
    }

    public static <E extends Enum<E>> E solicitarEnum(Scanner sc, String mensaje, Class<E> enumClass) {
        E[] valores = enumClass.getEnumConstants();
        System.out.println("\n" + mensaje + ":");
        for (int i = 0; i < valores.length; i++) {
            System.out.println("  " + (i + 1) + ". " + valores[i]);
        }
        System.out.println("  0. Cancelar");
        while (true) {
            System.out.print("\nOpción: ");
            String entrada = sc.nextLine().trim();
            if (entrada.equals(ABORTAR)) {
                System.out.println("Operación cancelada");
                return null;
            }
            try {
                int opcion = Integer.parseInt(entrada);
                if (opcion > 0 && opcion <= valores.length) {
                    return valores[opcion - 1];
                }
                System.err.println("Opción inválida");
            } catch (NumberFormatException e) {
                System.err.println("Ingrese un número");
            }
        }
    }

    public static String solicitarOpcion(Scanner sc, String mensaje, String[] opciones) {
        System.out.println("\n" + mensaje);
        while (true) {
            System.out.print("→ ");
            String entrada = sc.nextLine().trim().toUpperCase();
            if (entrada.equals(ABORTAR)) {
                System.out.println("Operación cancelada");
                return null;
            }
            for (String opcion : opciones) {
                if (opcion.equals(entrada)) {
                    return entrada;
                }
            }
            System.err.println("Opción inválida");
        }
    }
}
