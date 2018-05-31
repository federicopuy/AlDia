package com.example.federico.aldia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categoria {

    @SerializedName("horasTrabajo")
    @Expose
    private Integer horasTrabajo;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("monto")
    @Expose
    private Integer monto;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("tipoCategoria")
    @Expose
    private String tipoCategoria;

    /**
     * No args constructor for use in serialization
     *
     */
    public Categoria() {
    }

    /**
     *
     * @param nombre
     * @param id
     * @param horasTrabajo
     * @param tipoCategoria
     * @param monto
     */
    public Categoria(Integer horasTrabajo, long id, Integer monto, String nombre, String tipoCategoria) {
        super();
        this.horasTrabajo = horasTrabajo;
        this.id = id;
        this.monto = monto;
        this.nombre = nombre;
        this.tipoCategoria = tipoCategoria;
    }

    public Integer getHorasTrabajo() {
        return horasTrabajo;
    }

    public void setHorasTrabajo(Integer horasTrabajo) {
        this.horasTrabajo = horasTrabajo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

}