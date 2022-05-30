package com.example.inmobiliaria;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ModeloPropiedad implements Serializable {


    private int CodigoPostal;
    private int Baños;
    private int Habitaciones;
    private int Tamaño;
    private int Precio;
    private String Calle;
    private String Tipo;//Casa, apartamento Studio
    private String Ciudad;
    private String Categoria;//Alquiler venta
    private String Descripcion;
    private String Foto;
    private String key;

        @Exclude
        public String getKey(){
            return key;
        }
        @Exclude
        public void setKey(String Key) {
            key = Key;
        }


    public int getPrecio() {
        return Precio;
    }

    public void setPrecio(int precio) {
        Precio = precio;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public int getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        CodigoPostal = codigoPostal;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }
    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        this.Tipo = tipo;
    }

    public String getFoto() {     return Foto;}

    public void setFoto(String fotos) {this.Foto = fotos;}

    public int getBaños() {
        return Baños;
    }

    public void setBaños(int baños) {
        this.Baños = baños;
    }

    public int getHabitaciones() {
        return Habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        this.Habitaciones = habitaciones;
    }

    public int getTamaño() {
        return Tamaño;
    }

    public void setTamaño(int tamaño) {
        this.Tamaño = tamaño;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        this.Categoria = categoria;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }
    @Override
    public String toString() {
        return "ModeloPropiedad{" +
                "Calle=" + Calle +
                ", Ciudad=" + Ciudad +
                ", CodigoPostal='" + CodigoPostal + '\'' +
                ", tipo='" + Tipo + '\'' +
               ", fotos='" + Foto + '\'' +
                ", baños=" + Baños +
                ", habitaciones=" + Habitaciones +
                ", tamaño=" + Tamaño +
                ", categoria='" + Categoria + '\'' +
                ", descripcion='" + Descripcion + '\'' +
                ", precio='" +  Precio + '\'' +
                '}';
    }

    public ModeloPropiedad(){ }

    public ModeloPropiedad(String Calle, String Ciudad, int CodigoPostal, String tipo,String foto, int baños, int habitaciones, int tamaño, String categoria, String descripcion, int precio) {
        this.Calle = Calle;
        this.Ciudad = Ciudad;
        this.CodigoPostal = CodigoPostal;
        this.Tipo = tipo;
        this.Foto = foto;
        this.Baños = baños;
        this.Habitaciones = habitaciones;
        this.Tamaño = tamaño;
        this.Categoria = categoria;
        this.Descripcion = descripcion;
        this.Precio=precio;
        }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Calle", Calle);
        result.put("Ciudad", Ciudad);
        result.put("CodigoPostal", CodigoPostal);
        result.put("Tipo", Tipo);
        result.put("Foto", Foto);
        result.put("Baños", Baños);
        result.put("Habitaciones", Habitaciones);
        result.put("Tamaño", Tamaño);
        result.put("Categoria", Categoria);
        result.put("Descripcion", Descripcion);
        result.put("Precio", Precio);

        return result;
    }
}
