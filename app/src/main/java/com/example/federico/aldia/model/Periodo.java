package com.example.federico.aldia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class Periodo {

        @SerializedName("categoria")
        @Expose
        private Categoria categoria;
        @SerializedName("eliminado")
        @Expose
        private Boolean eliminado;
        @SerializedName("fechaLiquidacion")
        @Expose
        private String fechaLiquidacion;
        @SerializedName("horaFin")
        @Expose
        private String horaFin;
        @SerializedName("horaInicio")
        @Expose
        private String horaInicio;
        @SerializedName("id")
        @Expose
        private long id;

        /**
         * No args constructor for use in serialization
         *
         */
        public Periodo() {
        }

        /**
         *
         * @param id
         * @param categoria
         * @param horaInicio
         * @param eliminado
         * @param horaFin
         * @param fechaLiquidacion
         */
        public Periodo(Categoria categoria, Boolean eliminado, String fechaLiquidacion, String horaFin, String horaInicio, long id) {
            super();
            this.categoria = categoria;
            this.eliminado = eliminado;
            this.fechaLiquidacion = fechaLiquidacion;
            this.horaFin = horaFin;
            this.horaInicio = horaInicio;
            this.id = id;
        }

        public Categoria getCategoria() {
            return categoria;
        }

        public void setCategoria(Categoria categoria) {
            this.categoria = categoria;
        }

        public Boolean getEliminado() {
            return eliminado;
        }

        public void setEliminado(Boolean eliminado) {
            this.eliminado = eliminado;
        }

        public String getFechaLiquidacion() {
            return fechaLiquidacion;
        }

        public void setFechaLiquidacion(String fechaLiquidacion) {
            this.fechaLiquidacion = fechaLiquidacion;
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

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }