package com.example.edwardahn.assistant;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class EchoFragment extends Fragment {

    private static final String TAG = "Echo_Fragment";
    private Activity mActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return (LinearLayout) inflater.inflate(R.layout.fragment_echo, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}