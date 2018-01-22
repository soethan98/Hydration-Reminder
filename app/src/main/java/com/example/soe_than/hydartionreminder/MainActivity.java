package com.example.soe_than.hydartionreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soe_than.hydartionreminder.Utils.NotifiacationUtils;
import com.example.soe_than.hydartionreminder.sync.ReminderTask;
import com.example.soe_than.hydartionreminder.sync.ReminderUtilities;
import com.example.soe_than.hydartionreminder.sync.WaterReminderFirebaseJobService;
import com.example.soe_than.hydartionreminder.sync.WaterReminderIntentService;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mWaterCountDisplay;
    private TextView mChargingCountDisplay;
    private ImageView mChargingImageView;

    private Toast mToast;

    IntentFilter intentFilter;
    ChargingBroadcastReceiver chargingReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWaterCountDisplay = (TextView) findViewById(R.id.tv_water_count);
        mChargingCountDisplay = (TextView) findViewById(R.id.tv_charging_reminder_count);
        mChargingImageView = (ImageView) findViewById(R.id.iv_power_increment);

        updateWaterCount();
        updateChargingReminderCount();

        ReminderUtilities.scheduleChargingReminder(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        chargingReminder = new ChargingBroadcastReceiver();

        intentFilter = new IntentFilter();

        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);


    }

    private void showCharging(boolean isCharged) {
        if (isCharged) {
            mChargingImageView.setImageResource(R.drawable.ic_power_pink_80px);
        } else {
            mChargingImageView.setImageResource(R.drawable.ic_power_grey_80px);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            showCharging(batteryManager.isCharging());

        } else {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent currentBatteryStatus = this.registerReceiver(null, ifilter);
            int batteryfilter = currentBatteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = batteryfilter == BatteryManager.BATTERY_STATUS_CHARGING || batteryfilter == BatteryManager.BATTERY_STATUS_FULL;
            showCharging(isCharging);
        }

        registerReceiver(chargingReminder, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(chargingReminder);
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

    public void incrementWater(View view) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, getString(R.string.water_chug_toast), Toast.LENGTH_SHORT);
        mToast.show();

        Intent intentService = new Intent(MainActivity.this, WaterReminderIntentService.class);
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

    private class ChargingBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
            showCharging(isCharging);


        }
    }


}
