package com.example.federico.aldia.notifications;

import android.content.Context;

public class RemiderTask {

    public static final String ACTION_REMIND_SHIFT_END = "remind-shift-end";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";

    public static void executeTask(Context context, String action) {
        if (ACTION_REMIND_SHIFT_END.equals(action)) {
            issueReminder(context);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        }
    }

    private static void issueReminder(Context context) {
        NotificationUtils.remindUserBecausePeriodAboutToFinish(context);
    }
}
