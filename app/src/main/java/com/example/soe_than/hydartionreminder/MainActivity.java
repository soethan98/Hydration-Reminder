package com.example.soe_than.hydartionreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soe_than.hydartionreminder.Utils.NotifiacationUtils;
import com.example.soe_than.hydartionreminder.sync.ReminderTask;
import com.example.soe_than.hydartionreminder.sync.WaterReminderIntentService;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mWaterCountDisplay;
    private TextView mChargingCountDisplay;
    private ImageView mChargingImageView;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaterCountDisplay = (TextView) findViewById(R.id.tv_water_count);
        mChargingCountDisplay = (TextView) findViewById(R.id.tv_charging_reminder_count);
        mChargingImageView = (ImageView) findViewById(R.id.iv_power_increment);

        updateWaterCount();
        updateChargingReminderCount();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

    }

    private void updateWaterCount() {
        int waterCount = PreferenceUtilities.getWaterCount(MainActivity.this);
        mWaterCountDisplay.setText(waterCount + "");
    }

    private void updateChargingReminderCount() {
        int chargingReminders = PreferenceUtilities.getChargingReminderCount(this);
        String formattedChargingReminders = getResources().getQuantityString(
                R.plurals.charge_notification_count, chargingReminders, chargingReminders);
        mChargingCountDisplay.setText(formattedChargingReminders);

    }

    public void incrementWater(View view)
    {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, getString(R.string.water_chug_toast), Toast.LENGTH_SHORT);
        mToast.show();

        Intent intentService=new Intent(MainActivity.this, WaterReminderIntentService.class);
        intentService.setAction(ReminderTask.increment_water_count);
        startService(intentService);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (PreferenceUtilities.KEY_WATER_COUNT.equals(s)) {
            updateWaterCount();
        } else if (PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT.equals(s)) {
            updateChargingReminderCount();
        }

    }

    public void testNotification(View view)
    {
        NotifiacationUtils.remindUserBecauseCharging(this);
    }
}
