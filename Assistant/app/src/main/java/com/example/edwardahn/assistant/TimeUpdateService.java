package com.example.edwardahn.assistant;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by edwardahn on 8/9/15.
 */
public class TimeUpdateService extends Service {

    //TODO keep service and implement alarmmanager inside of it!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private static final String TAG = "TimeUpdateService";
    private BroadcastReceiver mReceiver = null;
    private final SimpleDateFormat time = new SimpleDateFormat("hh:mm");
    private final IBinder mBinder = new LocalBinder();
    private ServiceCallbacks serviceCallbacks;
    public static final int NOTIFICATION_ID = 23;

    public class LocalBinder extends Binder {
        TimeUpdateService getService() { return TimeUpdateService.this; }
    }

    public interface ServiceCallbacks {
        void sendTime();
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

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
    public IBinder onBind(Intent intent) {

        /*
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Neo")
                        .setContentText("Time Display");
        startForeground(NOTIFICATION_ID, mBuilder.build());*/

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    Log.i("","received action_time_tick intent");
                    if (serviceCallbacks != null) serviceCallbacks.sendTime();
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        //filter.addAction(Intent.ACTION_SCREEN_OFF);
        // LOLLIPOP NOW STOPS THIS FROM WORKING.
        registerReceiver(mReceiver, filter);

        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.i("", "register destroyed :(");
        //stopForeground(true);
        unregisterReceiver(mReceiver);
    }
}
