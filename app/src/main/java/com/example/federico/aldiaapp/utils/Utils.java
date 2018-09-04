package com.example.federico.aldiaapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.federico.aldiaapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String getDateAndHour(String fechaTimestamp, Context ctx) {
        String dateAndHour = "";
        try{
            dateAndHour = getDate(fechaTimestamp) + ctx.getString(R.string.at) + getHour(fechaTimestamp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dateAndHour;
    }

    public static String getDate(String fechaTimestamp) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        localDateFormat.setTimeZone(getTimeZone());
        return localDateFormat.format(formatDateinGMT(fechaTimestamp));
    }

    public static String getHour(String fechaTimestamp) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        localDateFormat.setTimeZone(getTimeZone());
        return localDateFormat.format(formatDateinGMT(fechaTimestamp));
    }

    public static String getEndOfShiftTime(Integer hoursOfWork) {
        Date currentTime = Calendar.getInstance().getTime();
        long workingHoursInMillis = TimeUnit.HOURS.toMillis(hoursOfWork);
        long finishWorkDateInMillis = currentTime.getTime() + workingHoursInMillis;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(finishWorkDateInMillis);
        return "Exit: " + formatter.format(calendar.getTime());
    }

    private static TimeZone getTimeZone() {
        return Calendar.getInstance().getTimeZone();
    }

    //https://stackoverflow.com/questions/5422089/date-timezone-conversion-in-java
    private static Date formatDateinGMT(String timestamp) {
        SimpleDateFormat sdfgmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdfgmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date inptdate = null;
        try {
            inptdate = sdfgmt.parse((timestamp.replace("T", " ")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inptdate;
    }

    public static String getTimeAndMoneyRegular(long hours, double moneyXHour) {
        double total = hours * moneyXHour;
        return hours + " hs - " + getFormattedAmount(total);
    }

    public static String getTimeAndMoneyExtra(long hours, double moneyXHour) {
        double total = hours * moneyXHour * 2;
        return hours + " hs - " + getFormattedAmount(total);
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

    public static String getFormattedAmount(Double total) {
        try {
            if (total != 0.0) {
                return "$ " + String.format(Locale.getDefault(), "%.2f", total);
            } else {
                return "$0.00";
            }
        } catch (Exception e){
            e.printStackTrace();
            return "$0.00";
        }
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

    public static String getJwtTokenFormatted(String response) {
        String tokenJWT = "";
        try {
            JSONObject root = new JSONObject(response);
            String bearer = root.getString("id_token");
            tokenJWT = "Bearer " + bearer;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tokenJWT;
    }
}
