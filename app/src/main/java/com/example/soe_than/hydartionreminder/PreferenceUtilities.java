package com.example.soe_than.hydartionreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.net.FileNameMap;

/**
 * Created by soe_than on 1/21/18.
 */

public final class PreferenceUtilities {
    public static final String KEY_WATER_COUNT="water_count";
   public static final String KEY_CHARGING_REMINDER_COUNT="charging_reminder_count";

   private static final int DEFAULT_COUNT=0;


   synchronized private static void setWaterCount(Context context,int glassOfWater)
   {
       SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
       SharedPreferences.Editor editor=prefs.edit();
       editor.putInt(KEY_WATER_COUNT,glassOfWater);
       editor.apply();
   }

   public static int getWaterCount(Context context)
   {
       SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
      int glassOfWater= prefs.getInt(KEY_WATER_COUNT,DEFAULT_COUNT);
      return  glassOfWater;
   }

   synchronized public static void incrementWaterCount(Context context)
   {
       int glassOfWater=PreferenceUtilities.getWaterCount(context);
       PreferenceUtilities.setWaterCount(context,++glassOfWater);
   }
   synchronized public static void chargingReminderCount(Context context)
   {
       SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(context);
       int chargingReminders=pref.getInt(KEY_CHARGING_REMINDER_COUNT,DEFAULT_COUNT);

       SharedPreferences.Editor editor=pref.edit();
       editor.putInt(KEY_CHARGING_REMINDER_COUNT,++chargingReminders);
       editor.apply();
   }


   public static int getChargingReminderCount(Context context)
   {
       SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(context);
      int chargingReminder= pref.getInt(KEY_CHARGING_REMINDER_COUNT,DEFAULT_COUNT);

      return  chargingReminder;
   }

}
