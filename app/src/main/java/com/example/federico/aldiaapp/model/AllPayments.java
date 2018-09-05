package com.example.federico.aldiaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllPayments {

    @SerializedName("content")
    @Expose
    private List<Payment> payment = null;
    @SerializedName("first")
    @Expose
    private Boolean first;
    @SerializedName("last")
    @Expose
    private Boolean last;
    @SerializedName("number")
    @Expose
    private long number;
    @SerializedName("numberOfElements")
    @Expose
    private long numberOfElements;
    @SerializedName("size")
    @Expose
    private long size;
    @SerializedName("totalElements")
    @Expose
    private long totalElements;
    @SerializedName("totalPages")
    @Expose
    private long totalPages;

    /**
     * No args constructor for use in serialization
     *
     */
    public AllPayments() {
    }

    /**
     *
     * @param payment
     * @param numberOfElements
     * @param last
     * @param totalElements
     * @param number
     * @param first
     * @param totalPages
     * @param size
     */
    public AllPayments(List<Payment> payment, Boolean first, Boolean last, long number, long numberOfElements, long size, long totalElements, long totalPages) {
        super();
        this.payment = payment;
        this.first = first;
        this.last = last;
        this.number = number;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<Payment> getPayment() {
        return payment;
    }

    public void setPayment(List<Payment> payment) {
        this.payment = payment;
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

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(long numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

}