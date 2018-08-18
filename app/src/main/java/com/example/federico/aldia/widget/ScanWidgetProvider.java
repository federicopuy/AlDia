package com.example.federico.aldia.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.federico.aldia.R;
import com.example.federico.aldia.activities.SignInActivity;
import com.example.federico.aldia.utils.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class ScanWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.scan_widget_provider);
        SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String textToDisplay = prefs.getString(Constants.KEY_INTENT_WIDGET, "");
        views.setTextViewText(R.id.appwidget_text, textToDisplay);
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(Constants.KEY_INTENT_WIDGET_BUTTON, "");
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.camera_widget_image, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

