package com.example.federico.aldia.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String obtenerFechaFormateada (String fechaTimestamp){

        String fechaHora = "";

        try{

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fechaTimestamp.replace("T"," "));

            String fecha = new SimpleDateFormat("dd-MM-yyyy").format(date);

            String hora = new SimpleDateFormat("HH:mm").format(date);

            fechaHora = fecha + " " + hora;

        }catch (Exception e){
            e.printStackTrace();
        }
            return fechaHora;

    }

    public static String obtenerHora(String fechaTimestamp){

        String hora = "";

        try{

            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fechaTimestamp.replace("T"," "));

            hora = new SimpleDateFormat("HH:mm").format(date);

        }catch (Exception e){
            e.printStackTrace();
        }

        return hora;

    }



}
