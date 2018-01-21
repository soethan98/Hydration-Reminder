package com.example.soe_than.hydartionreminder.sync;

import android.content.Context;

import com.example.soe_than.hydartionreminder.PreferenceUtilities;

/**
 * Created by soe_than on 1/21/18.
 */

public class ReminderTask {

    public static final String increment_water_count="water_count";


    public static void executeTask(Context context,String action)
    {
        if(increment_water_count.equals(action))
        {
            incrementWaterCount(context);
        }
    }

    private static void incrementWaterCount(Context context)
    {
        PreferenceUtilities.incrementWaterCount(context);
    }
}
