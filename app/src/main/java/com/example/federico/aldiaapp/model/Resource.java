package com.example.federico.aldiaapp.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public T data;
    @Nullable
    public String msg;

    public Resource(@NonNull Status status, @Nullable T data  ) {
        this.status = status;
        this.data = data;
    }

    public Resource(@NonNull Status status, @Nullable String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Resource(@NonNull Status status) {
        this.status = status;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.FAILED, data);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.RUNNING, data);
    }
}
