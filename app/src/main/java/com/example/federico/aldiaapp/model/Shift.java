package com.example.federico.aldiaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shift {

        @SerializedName("id")
        @Expose
        private Long id;
        @SerializedName("horaInicio")
        @Expose
        private String horaInicio;
        @SerializedName("horaFin")
        @Expose
        private String horaFin;
        @SerializedName("eliminado")
        @Expose
        private Boolean eliminado;
        @SerializedName("fechaLiquidacion")
        @Expose
        private String fechaLiquidacion;
        @SerializedName("categoria")
        @Expose
        private Position position;
        @SerializedName("user")
        @Expose
        private String user;
        @SerializedName("montoTotal")
        @Expose
        private Double montoTotal;
        @SerializedName("horasReg")
        @Expose
        private Long horasReg;
        @SerializedName("horasExt")
        @Expose
        private Long horasExt;
        @SerializedName("inasistencia")
        @Expose
        private Boolean inasistencia;

    public Shift() {
        }

        /**
         * @param id
         * @param inasistencia
         * @param position
         * @param horasExt
         * @param horaInicio
         * @param eliminado
         * @param horaFin
         * @param fechaLiquidacion
         * @param user
         * @param horasReg
         * @param montoTotal
         */
        public Shift(Long id, String horaInicio, String horaFin, Boolean eliminado, String fechaLiquidacion, Position position, String user, Double montoTotal, Long horasReg, Long horasExt, Boolean inasistencia) {
            super();
            this.id = id;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
            this.eliminado = eliminado;
            this.fechaLiquidacion = fechaLiquidacion;
            this.position = position;
            this.user = user;
            this.montoTotal = montoTotal;
            this.horasReg = horasReg;
            this.horasExt = horasExt;
            this.inasistencia = inasistencia;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getHoraInicio() {
            return horaInicio;
        }

        public void setHoraInicio(String horaInicio) {
            this.horaInicio = horaInicio;
        }

        public String getHoraFin() {
            return horaFin;
        }

        public void setHoraFin(String horaFin) {
            this.horaFin = horaFin;
        }

        public Boolean getEliminado() {
            return eliminado;
        }

        public void setEliminado(Boolean eliminado) {
            this.eliminado = eliminado;
        }

        public Object getFechaLiquidacion() {
            return fechaLiquidacion;
        }

        public void setFechaLiquidacion(String fechaLiquidacion) {
            this.fechaLiquidacion = fechaLiquidacion;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public Double getMontoTotal() {
            return montoTotal;
        }

        public void setMontoTotal(Double montoTotal) {
            this.montoTotal = montoTotal;
        }

        public Long getHorasReg() {
            return horasReg;
        }

        public void setHorasReg(Long horasReg) {
            this.horasReg = horasReg;
        }

        public Long getHorasExt() {
            return horasExt;
        }

        public void setHorasExt(Long horasExt) {
            this.horasExt = horasExt;
        }

        public Boolean getInasistencia() {
            return inasistencia;
        }

        public void setInasistencia(Boolean inasistencia) {
            this.inasistencia = inasistencia;
        }
    }