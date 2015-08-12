package com.example.edwardahn.assistant;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by edwardahn on 8/11/15.
 */
public class TimeUpdateReceiver extends BroadcastReceiver {

    private ReceiverCallbacks receiverCallbacks;

    private void scheduleNextAlarm(Context context) {
        return;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleNextAlarm(context);
    }

    public interface ReceiverCallbacks {
        void sendTime();
    }

    public void setCallbacks(ReceiverCallbacks callbacks) {
        receiverCallbacks = callbacks;
    }
}
