package com.example.federico.aldia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class Periodo {

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
        private Categoria categoria;
        @SerializedName("user")
        @Expose
        private String user;
        @SerializedName("montoTotal")
        @Expose
        private Long montoTotal;
        @SerializedName("horasReg")
        @Expose
        private Long horasReg;
        @SerializedName("horasExt")
        @Expose
        private Long horasExt;
        @SerializedName("inasistencia")
        @Expose
        private Boolean inasistencia;

        public Periodo() {
        }

        /**
         * @param id
         * @param inasistencia
         * @param categoria
         * @param horasExt
         * @param horaInicio
         * @param eliminado
         * @param horaFin
         * @param fechaLiquidacion
         * @param user
         * @param horasReg
         * @param montoTotal
         */
        public Periodo(Long id, String horaInicio, String horaFin, Boolean eliminado, String fechaLiquidacion, Categoria categoria, String user, Long montoTotal, Long horasReg, Long horasExt, Boolean inasistencia) {
            super();
            this.id = id;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
            this.eliminado = eliminado;
            this.fechaLiquidacion = fechaLiquidacion;
            this.categoria = categoria;
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

        public Categoria getCategoria() {
            return categoria;
        }

        public void setCategoria(Categoria categoria) {
            this.categoria = categoria;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public Long getMontoTotal() {
            return montoTotal;
        }

        public void setMontoTotal(Long montoTotal) {
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