package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Producto {

    private int id_producto;
    private String nombre;
    private BigDecimal precio;
    private int id_categoria;
    private Categoria categoria; // Objeto relacionado para manejar la relación

    public Producto() {}

    public Producto(String nombre, BigDecimal precio, int id_categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.id_categoria = id_categoria;
    }

    public Producto(int id_producto, String nombre, BigDecimal precio, int id_categoria) {
        this(nombre, precio, id_categoria);
        this.id_producto = id_producto;
    }

    // Constructor adicional que acepta un objeto Categoria
    public Producto(String nombre, BigDecimal precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        if (categoria != null) {
            this.id_categoria = categoria.getId_categoria();
        }
    }

    // Constructor completo con objeto Categoria
    public Producto(int id_producto, String nombre, BigDecimal precio, Categoria categoria) {
        this(nombre, precio, categoria);
        this.id_producto = id_producto;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        if (categoria != null) {
            this.id_categoria = categoria.getId_categoria();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id_producto == producto.id_producto &&
               id_categoria == producto.id_categoria &&
               Objects.equals(nombre, producto.nombre) &&
               Objects.equals(precio, producto.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_producto, nombre, precio, id_categoria);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id_producto=" + id_producto +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", id_categoria=" + id_categoria +
                '}';
    }
}