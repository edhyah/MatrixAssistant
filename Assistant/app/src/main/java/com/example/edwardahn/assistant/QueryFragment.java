package com.example.edwardahn.assistant;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class QueryFragment extends Fragment implements View.OnClickListener {

    public static final String label = "_ECHO_";

    ImageButton mButton;
    protected static final int REQUEST_OK = 5;

    // Layout views
    private ListView mConversationView;
    private EditText mOutEditText;
    private ImageButton mSendButton;

    // Array adapter for conversation thread
    public ArrayAdapter<String> mConversationArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = (LinearLayout) inflater.inflate(R.layout.fragment_query, container, false);
        mButton = (ImageButton) view.findViewById(R.id.button_voice);
        mButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(intent, REQUEST_OK);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OK) {
            if (data == null) return;
            ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ((TextView) getView().findViewById(R.id.edit_text_out)).setText(thingsYouSaid.get(0));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (ImageButton) view.findViewById(R.id.button_send);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    if (message.equals("")) return;
                    textView.setText("");
                    ((MainActivity) getActivity()).sendMessage(label+message+label);
                }
            }
        });
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                ((MainActivity) getActivity()).sendMessage(label+message+label);
            }
            return true;
        }
    };

    // QA capabilities

    private QAService qa = new QAService(10000);


    class QAThread extends Thread {
        String inputText;

        public QAThread(String txt) {
            inputText = txt;
        }

        public void run() {

            if (inputText == null || inputText.length() == 0)
                return;

            qa.runQA(inputText, getLocation());
            //setHtml(va.getText(), va.getImageUrl());
        }
    }

    private String getLocation() {
        String location = null;
        try {
            LocationManager locationManager = (LocationManager) getActivity()
                    .getSystemService(Context.LOCATION_SERVICE);
            Location l = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            location = l.getLatitude() + "," + l.getLongitude();
        } catch (Exception ex) {
        }
        return location;
    }
}