package com.example.soe_than.hydartionreminder.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by soe_than on 1/21/18.
 */

public class WaterReminderIntentService extends IntentService {


    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       if(intent!=null)
       {
           String action=intent.getAction();
           ReminderTask.executeTask(this,action);
       }

    }
}
