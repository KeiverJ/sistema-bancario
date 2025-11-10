package com.example.controller;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputUtils {
    private static final Logger logger = LoggerFactory.getLogger(InputUtils.class);
    private static final String ABORTAR = "0";
    private static final String OPERACION_CANCELADA = "Operación cancelada";

    private InputUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String solicitarTexto(Scanner sc, String mensaje, boolean obligatorio) {
        while (true) {
            logger.info("{}{} [0=cancelar]: ", mensaje, (obligatorio ? "" : " (opcional)"));
            String valor = sc.nextLine().trim();
            if (valor.equals(ABORTAR)) {
                logger.info(OPERACION_CANCELADA);
                logger.info(OPERACION_CANCELADA);
                return null;
            }
            if (!valor.isEmpty() || !obligatorio) {
                return valor;
            }
            logger.error("Campo obligatorio");
        }
    }

    public static Double solicitarMonto(Scanner sc, String mensaje) {
        while (true) {
            logger.info("{} [0=cancelar]: $", mensaje);
            String entrada = sc.nextLine().trim();
            if (entrada.equals(ABORTAR)) {
                logger.info(OPERACION_CANCELADA);
                return null;
            }
            try {
                double monto = Double.parseDouble(entrada);
                if (monto > 0) {
                    return monto;
                }
                logger.error("Debe ser mayor a cero");
            } catch (NumberFormatException e) {
                logger.error("Monto inválido");
            }
        }
    }

    public static Integer solicitarEnteroPositivo(Scanner sc, String mensaje) {
        while (true) {
            logger.info("{} [0=cancelar]: ", mensaje);
            String entrada = sc.nextLine().trim();
            if (entrada.equals(ABORTAR)) {
                logger.info(OPERACION_CANCELADA);
                return null;
            }
            try {
                int valor = Integer.parseInt(entrada);
                if (valor > 0) {
                    return valor;
                }
                logger.error("Debe ser mayor a cero");
            } catch (NumberFormatException e) {
                logger.error("Valor inválido");
            }
        }
    }

    public static <E extends Enum<E>> E solicitarEnum(Scanner sc, String mensaje, Class<E> enumClass) {
        E[] valores = enumClass.getEnumConstants();
    logger.info("\n{}:", mensaje);
        for (int i = 0; i < valores.length; i++) {
            logger.info("  {}. {}", (i + 1), valores[i]);
        }
    logger.info("  0. Cancelar");
        while (true) {
            logger.info("\nOpción: ");
            String entrada = sc.nextLine().trim();
            if (entrada.equals(ABORTAR)) {
                logger.info(OPERACION_CANCELADA);
                return null;
            }
            try {
                int opcion = Integer.parseInt(entrada);
                if (opcion > 0 && opcion <= valores.length) {
                    return valores[opcion - 1];
                }
                logger.error("Opción inválida");
            } catch (NumberFormatException e) {
                logger.error("Ingrese un número");
            }
        }
    }

    public static String solicitarOpcion(Scanner sc, String mensaje, String[] opciones) {
    logger.info("\n{}", mensaje);
        while (true) {
            logger.info("→ ");
            String entrada = sc.nextLine().trim().toUpperCase();
            if (entrada.equals(ABORTAR)) {
                logger.info(OPERACION_CANCELADA);
                return null;
            }
            for (String opcion : opciones) {
                if (opcion.equals(entrada)) {
                    return entrada;
                }
            }
            logger.error("Opción inválida");
        }
    }
}
