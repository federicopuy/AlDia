package com.example.federico.aldia.data;

public class Liquidacion {


    private String fecha;
    private String montoTotal;

    public Liquidacion(String fecha, String montoTotal) {
        this.fecha = fecha;
        this.montoTotal = montoTotal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(String montoTotal) {
        this.montoTotal = montoTotal;
    }
}
