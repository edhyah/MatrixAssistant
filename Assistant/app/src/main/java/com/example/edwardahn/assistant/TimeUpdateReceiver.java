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

    private static final String TAG = "TimeUpdateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Alarm received");
        MainActivity instance = MainActivity.instance;
        instance.scheduleNextAlarm();
        Log.i(TAG, "Next alarm scheduled");
        instance.sendTime();
    }

}
