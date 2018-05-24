package com.example.raul.inventario;

public class Producto {

    String nombre, descripcion, imagen;
    float precio;

    public Producto(String pNombre, float pPrecio, String pDescripcion, String pImagen) {
        this.nombre = pNombre;
        this.precio = pPrecio;
        this.descripcion = pDescripcion;
        this.imagen = pImagen;
    }

    public Producto() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
