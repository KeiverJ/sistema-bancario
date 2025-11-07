package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * BankConfig - Configuración del banco.
 * Spring Boot maneja automáticamente el Singleton.
 */
@Configuration
@ConfigurationProperties(prefix = "bank")
public class BankConfig {

    private final Properties properties;
    private String name;
    private String code;
    private String country;
    private String currency;
    private double tasaInteresBase;
    private int scoreMinimo;
    private double montoMaximoCredito;
    private double ratioEndeudamientoMaximo;

    public BankConfig() {
        this.properties = new Properties();
        // Valores por defecto
        this.name = "Banco del Futuro";
        this.code = "BDF-001";
        this.country = "Colombia";
        this.currency = "COP";
        this.tasaInteresBase = 12.5;
        this.scoreMinimo = 650;
        this.montoMaximoCredito = 500000000.0;
        this.ratioEndeudamientoMaximo = 0.40;

        loadDefaultProperties();
    }

    private void loadDefaultProperties() {
        properties.setProperty("bank.name", name);
        properties.setProperty("bank.code", code);
        properties.setProperty("bank.country", country);
        properties.setProperty("bank.currency", currency);
        properties.setProperty("credito.tasa.base", String.valueOf(tasaInteresBase));
        properties.setProperty("credito.score.minimo", String.valueOf(scoreMinimo));
        properties.setProperty("credito.monto.maximo", String.valueOf(montoMaximoCredito));
        properties.setProperty("credito.ratio.endeudamiento", String.valueOf(ratioEndeudamientoMaximo));
        properties.setProperty("credito.plazo.maximo", "360");
        properties.setProperty("cuenta.saldo.minimo.ahorros", "50000");
        properties.setProperty("cuenta.saldo.minimo.corriente", "100000");
        properties.setProperty("cuenta.cuota.manejo.ahorros", "10000");
        properties.setProperty("cuenta.cuota.manejo.corriente", "15000");
        properties.setProperty("transaccion.limite.diario", "10000000");
        properties.setProperty("transaccion.limite.mensual", "50000000");
        properties.setProperty("transaccion.comision.transferencia", "3000");
    }

    // Getters y Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        properties.setProperty("bank.name", name);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        properties.setProperty("bank.code", code);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        properties.setProperty("bank.country", country);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        properties.setProperty("bank.currency", currency);
    }

    public double getTasaInteresBase() {
        return tasaInteresBase;
    }

    public void setTasaInteresBase(double tasaInteresBase) {
        this.tasaInteresBase = tasaInteresBase;
        properties.setProperty("credito.tasa.base", String.valueOf(tasaInteresBase));
    }

    public int getScoreMinimo() {
        return scoreMinimo;
    }

    public void setScoreMinimo(int scoreMinimo) {
        this.scoreMinimo = scoreMinimo;
        properties.setProperty("credito.score.minimo", String.valueOf(scoreMinimo));
    }

    public double getMontoMaximoCredito() {
        return montoMaximoCredito;
    }

    public void setMontoMaximoCredito(double montoMaximoCredito) {
        this.montoMaximoCredito = montoMaximoCredito;
        properties.setProperty("credito.monto.maximo", String.valueOf(montoMaximoCredito));
    }

    public double getRatioEndeudamientoMaximo() {
        return ratioEndeudamientoMaximo;
    }

    public void setRatioEndeudamientoMaximo(double ratio) {
        this.ratioEndeudamientoMaximo = ratio;
        properties.setProperty("credito.ratio.endeudamiento", String.valueOf(ratio));
    }

    public String getProperty(String key) {
        return key == null ? null : properties.getProperty(key);
    }

    public Properties getAllProperties() {
        return new Properties(properties);
    }

    // Métodos de negocio

    public double getSaldoMinimo(String tipoCuenta) {
        String key = "cuenta.saldo.minimo." + tipoCuenta.toLowerCase();
        return getDoubleProperty(key, 0.0);
    }

    public double getCuotaManejo(String tipoCuenta) {
        String key = "cuenta.cuota.manejo." + tipoCuenta.toLowerCase();
        return getDoubleProperty(key, 0.0);
    }

    public double getLimiteDiario() {
        return getDoubleProperty("transaccion.limite.diario", 10000000.0);
    }

    public double getLimiteMensual() {
        return getDoubleProperty("transaccion.limite.mensual", 50000000.0);
    }

    public double getComisionTransferencia() {
        return getDoubleProperty("transaccion.comision.transferencia", 3000.0);
    }

    public int getPlazoMaximoCredito() {
        return getIntProperty("credito.plazo.maximo", 360);
    }

    private double getDoubleProperty(String key, double defaultValue) {
        String value = properties.getProperty(key, String.valueOf(defaultValue));
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean isMontoValido(double monto) {
        return monto > 0 && monto <= montoMaximoCredito;
    }

    public boolean isScoreAceptable(int score) {
        return score >= scoreMinimo;
    }

    public double calcularTasaPorScore(int score) {
        if (score >= 800)
            return tasaInteresBase * 0.7;
        if (score >= 750)
            return tasaInteresBase * 0.85;
        if (score >= 700)
            return tasaInteresBase;
        if (score >= 650)
            return tasaInteresBase * 1.2;
        return tasaInteresBase * 1.5;
    }

    public boolean isValidConfiguration() {
        return name != null && !name.isEmpty() &&
                code != null && !code.isEmpty() &&
                tasaInteresBase > 0 &&
                scoreMinimo > 0 &&
                montoMaximoCredito > 0 &&
                ratioEndeudamientoMaximo > 0 && ratioEndeudamientoMaximo <= 1.0;
    }

    @Override
    public String toString() {
        return String.format("BankConfig{name='%s', code='%s', tasaBase=%.2f%%, scoreMin=%d}",
                name, code, tasaInteresBase, scoreMinimo);
    }
}