package com.example.federico.aldiaapp.network;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "Error de Conexión";
    }
}
