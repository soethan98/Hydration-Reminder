package com.example.soe_than.hydartionreminder.sync;

import android.content.Context;

import com.example.soe_than.hydartionreminder.PreferenceUtilities;
import com.example.soe_than.hydartionreminder.Utils.NotifiacationUtils;

/**
 * Created by soe_than on 1/21/18.
 */

public class ReminderTask {

    public static final String increment_water_count = "water_count";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss_notification";


    public static void executeTask(Context context, String action) {
        if (increment_water_count.equals(action)) {
            incrementWaterCount(context);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotifiacationUtils.clearNotification(context);
        }
    }

    private static void incrementWaterCount(Context context) {
        PreferenceUtilities.incrementWaterCount(context);
        NotifiacationUtils.clearNotification(context);
    }
}
