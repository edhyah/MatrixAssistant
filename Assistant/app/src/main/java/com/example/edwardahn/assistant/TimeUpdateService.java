package com.example.edwardahn.assistant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by edwardahn on 8/9/15.
 */
public class TimeUpdateService extends Service {

    private static final String TAG = "TimeUpdateService";
    private BroadcastReceiver mReceiver = null;
    private final SimpleDateFormat time = new SimpleDateFormat("hh:mm");

    @Override
    public void onCreate() {
        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    /*
                    String currentTime = time.format(new Date());
                    if (currentTime.charAt(0) == '0')
                        currentTime = ' ' + currentTime.substring(1);
                    sendTime(currentTime);*/
                    // call sendmessage function in main activity
                }
            }
        };
        registerReceiver(mReceiver,new IntentFilter(Intent.ACTION_TIME_TICK));

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
    }
}
