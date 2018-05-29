package com.example.federico.aldia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllLiquidaciones {

    @SerializedName("content")
    @Expose
    private List<Liquidacion> liquidacion = null;
    @SerializedName("first")
    @Expose
    private Boolean first;
    @SerializedName("last")
    @Expose
    private Boolean last;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("numberOfElements")
    @Expose
    private Integer numberOfElements;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;

    /**
     * No args constructor for use in serialization
     *
     */
    public AllLiquidaciones() {
    }

    /**
     *
     * @param liquidacion
     * @param numberOfElements
     * @param last
     * @param totalElements
     * @param number
     * @param first
     * @param totalPages
     * @param size
     */
    public AllLiquidaciones(List<Liquidacion> liquidacion, Boolean first, Boolean last, Integer number, Integer numberOfElements, Integer size, Integer totalElements, Integer totalPages) {
        super();
        this.liquidacion = liquidacion;
        this.first = first;
        this.last = last;
        this.number = number;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<Liquidacion> getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(List<Liquidacion> liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}