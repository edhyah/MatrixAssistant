package com.example.edwardahn.assistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeFragment extends Fragment {

    private final SimpleDateFormat time = new SimpleDateFormat("hh:mm");
    private BroadcastReceiver mReceiver = null;
    private TextView mTextView;

    public static final String label = Constants.TIMES;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (LinearLayout) inflater.inflate(R.layout.fragment_time, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mTextView = (TextView) getView().findViewById(R.id.view_time);
        String currentTime = time.format(new Date());
        if (currentTime.charAt(0) == '0')
            currentTime = ' ' + currentTime.substring(1);
        mTextView.setText(currentTime);
    }

    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    String currentTime = time.format(new Date());
                    if (currentTime.charAt(0) == '0')
                        currentTime = ' ' + currentTime.substring(1);
                    mTextView.setText(currentTime);
                    sendTime(currentTime);
                }
            }
        };
        getActivity().registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        if ((MainActivity) getActivity() != null) sendTime();
    }

    public void sendTime(String currentTime) {
        ((MainActivity) getActivity()).sendMessage(label+currentTime+label);
    }

    public void sendTime() {
        String currentTime = time.format(new Date());
        if (currentTime.charAt(0) == '0')
            currentTime = ' ' + currentTime.substring(1);
        ((MainActivity) getActivity()).sendMessage(label+currentTime+label);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }
}