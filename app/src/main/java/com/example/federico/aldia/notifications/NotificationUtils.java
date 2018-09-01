package com.example.federico.aldia.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.federico.aldia.R;
import com.example.federico.aldia.activities.SignInActivity;

public class NotificationUtils {

    private static final int SHIFT_ENDING_NOTIFICATION_ID = 1138;
    private static final int SHIFT_ENDING_PENDING_INTENT_ID = 6351;
    private static final String SHIFT_ENDING_NOTIFICATION_CHANNEL_ID = "shift_ending_channel";

    public static void remindUserBecausePeriodAboutToFinish(Context context){

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    SHIFT_ENDING_NOTIFICATION_CHANNEL_ID, "Primary",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,SHIFT_ENDING_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_al_dia)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.shift_ending_notification_title))
                .setContentText(context.getString(R.string.shift_ending_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.shift_ending_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(SHIFT_ENDING_NOTIFICATION_ID, notificationBuilder.build());
    }


    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, SignInActivity.class);
        return PendingIntent.getActivity(
                context,
                SHIFT_ENDING_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.ic_al_dia);
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
