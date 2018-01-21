package com.example.soe_than.hydartionreminder.Utils;

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
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.content.ContextCompat;

import com.example.soe_than.hydartionreminder.MainActivity;
import com.example.soe_than.hydartionreminder.R;
import com.example.soe_than.hydartionreminder.sync.ReminderTask;
import com.example.soe_than.hydartionreminder.sync.WaterReminderIntentService;

/**
 * Created by soe_than on 1/21/18.
 */

public class NotifiacationUtils {

    public static final int WATER_REMINDER_PENDING_INTENT_ID = 3147;

    private static final int WATER_REMINDER_NOTIFICATION_ID = 1138;
    private static final int ACTION_DRINK_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;


    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    public static void clearNotification(Context context) {
        NotificationManager notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notiManager.cancelAll();
    }


    public static void remindUserBecauseCharging(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID
                    , context.getString(R.string.main_notification_channel_name)
                    , NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(getContentIntent(context))
                .addAction(ignoreReminderAction(context))
                .addAction(drinkWaterAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());

    }

    private static Action ignoreReminderAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, WaterReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTask.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(context, ACTION_IGNORE_PENDING_INTENT_ID
                , ignoreReminderIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Action ignoreReminderAction = new Action(R.drawable.ic_cancel_black_24px, "No..Thanks"
                , ignoreReminderPendingIntent);

        return ignoreReminderAction;


    }

    private static Action drinkWaterAction(Context context) {
        Intent drinkWaterIntent = new Intent(context, WaterReminderIntentService.class);
        drinkWaterIntent.setAction(ReminderTask.increment_water_count);
        PendingIntent drinkWaterPendingIntent = PendingIntent.getService(context
                , ACTION_DRINK_PENDING_INTENT_ID
                , drinkWaterIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        Action drinWaterAction = new Action(R.drawable.ic_drink_notification, "I Did It"
                , drinkWaterPendingIntent);

        return drinWaterAction;
    }

    private static PendingIntent getContentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context
                , WATER_REMINDER_PENDING_INTENT_ID
                , startActivityIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);
        return largeIcon;
    }
}
