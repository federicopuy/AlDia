package com.example.federico.aldia.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.QrToken;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String obtenerFechaFormateada (String fechaTimestamp){
        String fechaHora = "";
        try{
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fechaTimestamp.replace("T"," "));
            String fecha = new SimpleDateFormat("dd-MM-yyyy").format(date);
            String hora = new SimpleDateFormat("HH:mm").format(date);
            fechaHora = fecha + " a las " + hora;
        }catch (Exception e){
            e.printStackTrace();
        }
            return fechaHora;
    }

    public static String obtenerSoloFechaFormateada (String fechaTimestamp){

        String fecha = "";
        try{
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fechaTimestamp.replace("T"," "));
            fecha = new SimpleDateFormat("dd-MM-yyyy").format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return fecha;
    }

    public static String obtenerHoraYMontoRegular(long horas, double montoXHora){
        double montoTotal = horas * montoXHora;
        return horas + " hs - " + obtenerMontoFormateado(montoTotal);
    }

    public static String obtenerHoraYMontoExtra(long horas, double montoXHora){
        double montoTotal = horas * montoXHora * 2;
        return horas + " hs - " + obtenerMontoFormateado(montoTotal);
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

    public static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission granted: ", permission);
            return true;
        }
        Log.i("Permission granted: ", permission);
        return false;
    }

    public static String obtenerMontoFormateado(Double monto){

        //todo faltan dos decimales y error

        try {
            if (monto!=0.0) {
                return "$ " + String.format("%.2f", monto);
            } else {
                return "$0.00";

            }
        } catch (Exception e){
            e.printStackTrace();
            return "$0.00";

        }
    }

    public static String getEndOfShiftTime(Integer hoursOfWork){
        Date currentTime = Calendar.getInstance().getTime();
        long workingHoursInMillis = TimeUnit.HOURS.toMillis(hoursOfWork);
        long finishWorkDateInMillis = currentTime.getTime() + workingHoursInMillis;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(finishWorkDateInMillis);
        return formatter.format(calendar.getTime());
    }

    public static int minutesTillEndOfShift(Integer hoursOfWork) {
        long workingHoursInMillis = TimeUnit.HOURS.toMillis(hoursOfWork);
        long minutesTillEnd = TimeUnit.MILLISECONDS.toMinutes(workingHoursInMillis);
        return (int) minutesTillEnd;
    }

    public static String currentTimeToInstant(){

        Date currentTime = Calendar.getInstance().getTime();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Instant timestamp = currentTime.toInstant();
            return String.valueOf(timestamp);
        } else {
            return "";
        }

    }


}
