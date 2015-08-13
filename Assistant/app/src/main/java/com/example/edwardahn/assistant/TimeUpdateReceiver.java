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
        /*
        // runtime error!!!!!!!!!!
        ((MainActivity) context).scheduleNextAlarm();
        ((MainActivity) context).sendTime();*/
        MainActivity instance = MainActivity.instance;
        instance.scheduleNextAlarm();
        instance.sendTime();
    }

    /*
    08-13 11:03:00.069  10713-10713/com.example.edwardahn.assistant E/AndroidRuntime﹕ FATAL EXCEPTION: main
    Process: com.example.edwardahn.assistant, PID: 10713
    java.lang.RuntimeException: Unable to start receiver com.example.edwardahn.assistant.TimeUpdateReceiver: java.lang.ClassCastException: android.app.ReceiverRestrictedContext cannot be cast to com.example.edwardahn.assistant.MainActivity
    at android.app.ActivityThread.handleReceiver(ActivityThread.java:3114)
    at android.app.ActivityThread.access$1800(ActivityThread.java:181)
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1551)
    at android.os.Handler.dispatchMessage(Handler.java:102)
    at android.os.Looper.loop(Looper.java:145)
    at android.app.ActivityThread.main(ActivityThread.java:6117)
    at java.lang.reflect.Method.invoke(Native Method)
    at java.lang.reflect.Method.invoke(Method.java:372)
    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1399)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1194)
    Caused by: java.lang.ClassCastException: android.app.ReceiverRestrictedContext cannot be cast to com.example.edwardahn.assistant.MainActivity
    at com.example.edwardahn.assistant.TimeUpdateReceiver.onReceive(TimeUpdateReceiver.java:28)
    at android.app.ActivityThread.handleReceiver(ActivityThread.java:3107)
                        at android.app.ActivityThread.access$1800(ActivityThread.java:181)
                        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1551)
                        at android.os.Handler.dispatchMessage(Handler.java:102)
                        at android.os.Looper.loop(Looper.java:145)
                        at android.app.ActivityThread.main(ActivityThread.java:6117)
                        at java.lang.reflect.Method.invoke(Native Method)
                        at java.lang.reflect.Method.invoke(Method.java:372)
                        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1399)
                        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1194)*/

}
