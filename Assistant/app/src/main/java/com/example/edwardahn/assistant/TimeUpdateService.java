package com.example.edwardahn.assistant;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by edwardahn on 8/9/15.
 */
public class TimeUpdateService extends Service {

    private static final String TAG = "TimeUpdateService";

    @Override
    public void onCreate() {
        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onDestroy() {
        return;
    }
}
