package com.example.federico.aldia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Periodo {

    @SerializedName("eliminado")
    @Expose
    private Boolean eliminado;
    @SerializedName("empleadoId")
    @Expose
    private Integer empleadoId;
    @SerializedName("empleadoNombre")
    @Expose
    private String empleadoNombre;
    @SerializedName("horaFin")
    @Expose
    private String horaFin;
    @SerializedName("horaInicio")
    @Expose
    private String horaInicio;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("liquidacionId")
    @Expose
    private Integer liquidacionId;
    @SerializedName("user")
    @Expose
    private String user;

    /**
     * No args constructor for use in serialization
     *
     */
    public Periodo() {
    }

    /**
     *
     * @param id
     * @param liquidacionId
     * @param empleadoId
     * @param horaInicio
     * @param eliminado
     * @param horaFin
     * @param empleadoNombre
     * @param user
     */
    public Periodo(Boolean eliminado, Integer empleadoId, String empleadoNombre, String horaFin, String horaInicio, Integer id, Integer liquidacionId, String user) {
        super();
        this.eliminado = eliminado;
        this.empleadoId = empleadoId;
        this.empleadoNombre = empleadoNombre;
        this.horaFin = horaFin;
        this.horaInicio = horaInicio;
        this.id = id;
        this.liquidacionId = liquidacionId;
        this.user = user;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getEmpleadoNombre() {
        return empleadoNombre;
    }

    public void setEmpleadoNombre(String empleadoNombre) {
        this.empleadoNombre = empleadoNombre;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLiquidacionId() {
        return liquidacionId;
    }

    public void setLiquidacionId(Integer liquidacionId) {
        this.liquidacionId = liquidacionId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}