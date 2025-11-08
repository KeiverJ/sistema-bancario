package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cliente del sistema.
 */
public class Cliente {

  public enum TipoCliente {
    PERSONA_NATURAL, PERSONA_JURIDICA, EXTRANJERO
  }

  private String id;
  private String codigo;
  private String nombre;
  private String tipoDocumento;
  private String numeroDocumento;
  private TipoCliente tipoCliente;
  private String email;
  private String telefono;
  private final List<String> cuentaIds = new ArrayList<>();
  private final List<String> creditoIds = new ArrayList<>();
  private int scoreActual = 600;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getScoreActual() {
    return scoreActual;
  }

  public void setScoreActual(int scoreActual) {
    this.scoreActual = scoreActual;
  }

  public void mejorarScore(int puntos) {
    this.scoreActual = Math.min(850, this.scoreActual + puntos);
  }

  public void reducirScore(int puntos) {
    this.scoreActual = Math.max(300, this.scoreActual - puntos);
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getNumeroDocumento() {
    return numeroDocumento;
  }

  public void setNumeroDocumento(String numeroDocumento) {
    this.numeroDocumento = numeroDocumento;
  }

  public TipoCliente getTipoCliente() {
    return tipoCliente;
  }

  public void setTipoCliente(TipoCliente tipoCliente) {
    this.tipoCliente = tipoCliente;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public List<String> getCuentaIds() {
    return List.copyOf(cuentaIds);
  }

  public void agregarCuenta(String cuentaId) {
    if (!this.cuentaIds.contains(cuentaId)) {
      this.cuentaIds.add(cuentaId);
    }
  }

  public List<String> getCreditoIds() {
    return List.copyOf(creditoIds);
  }

  public void agregarCredito(String creditoId) {
    if (creditoId != null)
      creditoIds.add(creditoId);
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

}