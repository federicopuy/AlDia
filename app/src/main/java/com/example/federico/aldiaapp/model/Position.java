package com.example.federico.aldiaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position {

        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("nombre")
        @Expose
        private String nombre;
        @SerializedName("horasTrabajo")
        @Expose
        private Integer horasTrabajo;
        @SerializedName("monto")
        @Expose
        private double monto;
        @SerializedName("tipoCategoria")
        @Expose
        private String tipoCategoria;
        @SerializedName("diasTrabajo")
        @Expose
        private int diasTrabajo;

        /**
         * No args constructor for use in serialization
         *
         */
        public Position() {
        }

        /**
         *  @param id
         * @param nombre
         * @param horasTrabajo
         * @param monto
         * @param tipoCategoria
         * @param diasTrabajo
         */
        public Position(long id, String nombre, Integer horasTrabajo, double monto, String tipoCategoria, int diasTrabajo) {
            super();
            this.id = id;
            this.nombre = nombre;
            this.horasTrabajo = horasTrabajo;
            this.monto = monto;
            this.tipoCategoria = tipoCategoria;
            this.diasTrabajo = diasTrabajo;
        }

        public long getId() {
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

        public Integer getHorasTrabajo() {
            return horasTrabajo;
        }

        public void setHorasTrabajo(Integer horasTrabajo) {
            this.horasTrabajo = horasTrabajo;
        }

        public double getMonto() {
            return monto;
        }

        public void setMonto(Integer monto) {
            this.monto = monto;
        }

        public String getTipoCategoria() {
            return tipoCategoria;
        }

        public void setTipoCategoria(String tipoCategoria) {
            this.tipoCategoria = tipoCategoria;
        }

        public Object getDiasTrabajo() {
            return diasTrabajo;
        }

        public void setDiasTrabajo(int diasTrabajo) {
            this.diasTrabajo = diasTrabajo;
        }

    }
