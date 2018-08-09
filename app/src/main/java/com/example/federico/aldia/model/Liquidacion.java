package com.example.federico.aldia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Liquidacion {

    @SerializedName("categoria")
    @Expose
    private Categoria categoria;
    @SerializedName("employee")
    @Expose
    private Employee employee;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("horasTotExt")
    @Expose
    private Integer horasTotExt;
    @SerializedName("horasTotReg")
    @Expose
    private Integer horasTotReg;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("montoTotal")
    @Expose
    private Double montoTotal;

    /**
     * No args constructor for use in serialization
     *
     */
    public Liquidacion() {
    }

    /**
     *
     * @param id
     * @param horasTotReg
     * @param horasTotExt
     * @param categoria
     * @param fecha
     * @param employee
     * @param montoTotal
     */
    public Liquidacion(Categoria categoria, Employee employee, String fecha, Integer horasTotExt, Integer horasTotReg, long id, Double montoTotal) {
        super();
        this.categoria = categoria;
        this.employee = employee;
        this.fecha = fecha;
        this.horasTotExt = horasTotExt;
        this.horasTotReg = horasTotReg;
        this.id = id;
        this.montoTotal = montoTotal;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getHorasTotExt() {
        return horasTotExt;
    }

    public void setHorasTotExt(Integer horasTotExt) {
        this.horasTotExt = horasTotExt;
    }

    public Integer getHorasTotReg() {
        return horasTotReg;
    }

    public void setHorasTotReg(Integer horasTotReg) {
        this.horasTotReg = horasTotReg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }

}