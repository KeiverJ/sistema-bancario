package com.example.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.example.decorator.core.ProductoFinancieroComponent;

public class PaqueteProductos implements ProductoFinancieroComponent {

    private final String id = UUID.randomUUID().toString();
    private final String nombre;
    private final List<ProductoFinancieroComponent> items = new ArrayList<>();

    public PaqueteProductos(String nombre) {
        this.nombre = nombre;
    }

    public PaqueteProductos add(ProductoFinancieroComponent component) {
        if (component != null)
            items.add(component);
        return this;
    }

    public PaqueteProductos remove(ProductoFinancieroComponent component) {
        items.remove(component);
        return this;
    }

    public List<ProductoFinancieroComponent> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getCostoMensual() {
        return items.stream().mapToDouble(ProductoFinancieroComponent::getCostoMensual).sum();
    }

    @Override
    public double getBeneficioMensual() {
        return items.stream().mapToDouble(ProductoFinancieroComponent::getBeneficioMensual).sum();
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        StringBuilder sb = new StringBuilder();
        sb.append("📦 Paquete: ").append(nombre).append("\n");
        sb.append("Productos incluidos:\n");
        for (ProductoFinancieroComponent comp : items) {
            sb.append("\n").append(comp.getDescripcion());
        }
        return sb.toString();
    }
}