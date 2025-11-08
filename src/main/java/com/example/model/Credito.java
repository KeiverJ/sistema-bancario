package com.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.state.CreditoState;
import com.example.state.StateFactory;

/**
 * Crédito otorgado al cliente.
 */
@Document(collection = "creditos")
public class Credito extends ProductoFinanciero {

    public enum TipoCredito {
        CONSUMO, LIBRE_INVERSION, HIPOTECARIO, VEHICULO
    }

    public enum EstadoCredito {
        SOLICITADO,
        APROBADO,
        RECHAZADO,
        DESEMBOLSADO,
        ACTIVO,
        EN_MORA,
        CANCELADO
    }

    @Id
    private String id;
    private String codigo;
    // clienteId se hereda de ProductoFinanciero

    private TipoCredito tipoCredito;
    private double monto;
    private double saldo;
    private int plazoMeses;
    private double tasaInteres;

    private boolean seguroVida;
    private boolean seguroDesempleo;
    private String garantia;
    private double costoApertura;
    private double cuotaAdministracion;

    private EstadoCredito estadoActual = EstadoCredito.SOLICITADO;

    // State (no serializable)
    private transient CreditoState state;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // clienteId se hereda de ProductoFinanciero
    // No necesitamos redefinir getClienteId() y setClienteId()

    public TipoCredito getTipoCredito() {
        return tipoCredito;
    }

    public void setTipoCredito(TipoCredito tipoCredito) {
        this.tipoCredito = tipoCredito;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public EstadoCredito getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoCredito estadoActual) {
        this.estadoActual = estadoActual;
        this.state = null; // se reconstruye lazy
    }

    // Sobrecarga para compatibilidad con String
    public void setEstadoActual(String estadoStr) {
        try {
            this.estadoActual = EstadoCredito.valueOf(estadoStr);
            this.state = null;
        } catch (IllegalArgumentException e) {
            this.estadoActual = EstadoCredito.SOLICITADO;
            this.state = null;
        }
    }

    public boolean isSeguroVida() {
        return seguroVida;
    }

    public void setSeguroVida(boolean seguroVida) {
        this.seguroVida = seguroVida;
    }

    public boolean isSeguroDesempleo() {
        return seguroDesempleo;
    }

    public void setSeguroDesempleo(boolean seguroDesempleo) {
        this.seguroDesempleo = seguroDesempleo;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public double getCostoApertura() {
        return costoApertura;
    }

    public void setCostoApertura(double costoApertura) {
        this.costoApertura = costoApertura;
    }

    public double getCuotaAdministracion() {
        return cuotaAdministracion;
    }

    public void setCuotaAdministracion(double cuotaAdministracion) {
        this.cuotaAdministracion = cuotaAdministracion;
    }

    public void initStateIfNull() {
        if (state == null) {
            this.state = StateFactory.from(this.estadoActual.name());
            if (this.state == null) {
                this.state = StateFactory.from("SOLICITADO");
            }
        }
    }

    public void setState(CreditoState newState) {
        this.state = newState;
        this.estadoActual = EstadoCredito.valueOf(newState.nombre());
    }

    // Acciones delegadas al estado
    public void aprobar() {
        initStateIfNull();
        state.aprobar(this);
    }

    public void rechazar() {
        initStateIfNull();
        state.rechazar(this, "No cumple requisitos");
    }

    public void desembolsar() {
        initStateIfNull();
        state.desembolsar(this);
    }

    public void marcarMora() {
        initStateIfNull();
        state.marcarMora(this);
    }

    public void cerrar() {
        initStateIfNull();
        state.cerrar(this);
    }

    public boolean pagarCuota(double montoPago) {
        initStateIfNull();
        return state.pagar(this, montoPago);
    }
}