package com.example.federico.aldiaapp.model;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("categoria")
    @Expose
    private Position position;
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

    public static DiffUtil.ItemCallback<Payment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Payment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Payment oldItem, @NonNull Payment newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Payment oldItem, @NonNull Payment newItem) {
            return oldItem.equals(newItem);
        }
    };

    /**
     * No args constructor for use in serialization
     *
     */
    public Payment() {
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    /**
     * @param id
     * @param horasTotReg
     * @param horasTotExt
     * @param position
     * @param fecha
     * @param employee
     * @param montoTotal
     */
    public Payment(Position position, Employee employee, String fecha, Integer horasTotExt, Integer horasTotReg, long id, Double montoTotal) {
        super();
        this.position = position;
        this.employee = employee;
        this.fecha = fecha;
        this.horasTotExt = horasTotExt;
        this.horasTotReg = horasTotReg;
        this.id = id;
        this.montoTotal = montoTotal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Payment payment = (Payment) obj;
        return payment.id == this.id;
    }
}