package com.example.federico.aldia.network;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "Error de Conexión";
    }
}
