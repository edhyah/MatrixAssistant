package com.example.edwardahn.assistant;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by edwardahn on 8/11/15.
 */
public class TimeUpdateReceiver extends BroadcastReceiver {

    //private AlarmManager mAlarmManager;

    private static final String TAG = "TimeUpdateReceiver";

    /*
    private void scheduleNextAlarm(Context context) {
        Intent intent = new Intent(this, TimeUpdateReceiver.class);
        return;
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm Receieved");
        ((MainActivity) context).scheduleNextAlarm();
        ((MainActivity) context).sendTime();
    }

}
