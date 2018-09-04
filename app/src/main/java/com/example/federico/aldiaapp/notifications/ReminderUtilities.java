package com.example.federico.aldiaapp.notifications;

import android.content.Context;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class ReminderUtilities {

    private static final int SYNC_FLEXTIME_SECONDS = 600;
    private static final String REMINDER_JOB_TAG = "shift_ending_reminder_tag";
    private static boolean sInitialized;

    synchronized public static void scheduleShiftEndReminder(Context context, int minutesToSendReminder) {
        if (sInitialized) return;
        final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(minutesToSendReminder));
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(ShiftEndingFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(false)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.mustSchedule(constraintReminderJob);
        sInitialized = true;
    }
}

