package com.example.federico.aldia.data;

/**
 * Created by Federico on 16/04/2018.
 */

public class Periodo {

    String id;
    String horaIngreso;
    String horaEgreso;
    String fecha;

    String horasRegularesTotales;
    String horasExtraTotales;
    String recaudadoHorasRegulares;
    String recaudadoHorasExtra;

    public Periodo(String id, String horaIngreso, String horaEgreso, String fecha) {
        this.id = id;
        this.horaIngreso = horaIngreso;
        this.horaEgreso = horaEgreso;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public String getHoraEgreso() {
        return horaEgreso;
    }

    public void setHoraEgreso(String horaEgreso) {
        this.horaEgreso = horaEgreso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorasRegularesTotales() {
        return horasRegularesTotales;
    }

    public void setHorasRegularesTotales(String horasRegularesTotales) {
        this.horasRegularesTotales = horasRegularesTotales;
    }

    public String getHorasExtraTotales() {
        return horasExtraTotales;
    }

    public void setHorasExtraTotales(String horasExtraTotales) {
        this.horasExtraTotales = horasExtraTotales;
    }

    public String getRecaudadoHorasRegulares() {
        return recaudadoHorasRegulares;
    }

    public void setRecaudadoHorasRegulares(String recaudadoHorasRegulares) {
        this.recaudadoHorasRegulares = recaudadoHorasRegulares;
    }

    public String getRecaudadoHorasExtra() {
        return recaudadoHorasExtra;
    }

    public void setRecaudadoHorasExtra(String recaudadoHorasExtra) {
        this.recaudadoHorasExtra = recaudadoHorasExtra;
    }
}
