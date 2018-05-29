package com.example.federico.aldia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Empleado {

    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("dni")
    @Expose
    private String dni;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("telefono")
    @Expose
    private String telefono;

    /**
     * No args constructor for use in serialization
     *
     */
    public Empleado() {
    }

    /**
     *
     * @param nombre
     * @param id
     * @param direccion
     * @param email
     * @param telefono
     * @param dni
     */
    public Empleado(String direccion, String dni, String email, Integer id, String nombre, String telefono) {
        super();
        this.direccion = direccion;
        this.dni = dni;
        this.email = email;
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
