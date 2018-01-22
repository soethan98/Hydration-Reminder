package com.example.soe_than.hydartionreminder.sync;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.sql.Driver;
import java.util.concurrent.TimeUnit;

/**
 * Created by soe_than on 1/21/18.
 */

public class ReminderUtilities {

    private static final int REMINDER_INTERVAL_MINUTES = 60;
    private static final int REMINDER_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES);
    private static final int SYNC_FLEXING_SECOND = REMINDER_INTERVAL_MINUTES;

    private static final String REMINDER_JOB_TAG = "hydration_reminder_tag";


    private static boolean isInitialized;

    synchronized public static void scheduleChargingReminder(Context context) {
        if (isInitialized) return;
        GooglePlayDriver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(WaterReminderFirebaseJobService.class)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setTag(REMINDER_JOB_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXING_SECOND))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);
        isInitialized = true;

    }
}
