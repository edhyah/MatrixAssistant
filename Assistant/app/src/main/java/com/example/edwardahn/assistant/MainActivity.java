package com.example.edwardahn.assistant;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CONNECT_DEVICE = 2;

    // Handles all Bluetooth connections
    private BluetoothService mBluetoothService = null;

    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;

    // Current fragment codes
    private static final int CURRENT_TIME = 0;
    private static final int CURRENT_ECHO = 1;
    private static final int CURRENT_QUERY = 2;
    private static int currentFragment = CURRENT_TIME;

    // for Alarm receiver and service
    private final SimpleDateFormat time = new SimpleDateFormat("hh:mm");
    private PendingIntent mPendingIntent;
    public AlarmManager mAlarmManager;
    public static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        // Set the Action Bar to use tabs for navigation
        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Add three tabs to the Action Bar for display
        ab.addTab(ab.newTab().setText("Time").setTabListener(
                new TabListener<TimeFragment>(this, "time", TimeFragment.class)));
        ab.addTab(ab.newTab().setText("Echo").setTabListener(
                new TabListener<EchoFragment>(this, "echo", EchoFragment.class)));
        ab.addTab(ab.newTab().setText("Query").setTabListener(
                new TabListener<QueryFragment>(this, "query", QueryFragment.class)));

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

        // Enable Bluetooth
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Register for broadcasts on BluetoothAdapter state change
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    private void setupConnection() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONNECT_DEVICE && resultCode == Activity.RESULT_OK) {
            connectDevice(data);
        }
    }

    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService = new BluetoothService(this, mHandler);
        mBluetoothService.connect(device);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu resource (res/menu/main)
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_connect:
                setupConnection();
        }
        return super.onOptionsItemSelected(item);
    }

    // Service methods

    public void scheduleNextAlarm() {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, TimeUpdateReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        long next = System.currentTimeMillis() + 60000; // 60000 millis == 1 minute
        long nextMinAtZero = next - (next % 60000);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, nextMinAtZero, mPendingIntent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, TimeUpdateReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        mAlarmManager.cancel(mPendingIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getCurrentFragment() != CURRENT_TIME) sendTime();
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, TimeUpdateReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        long next = System.currentTimeMillis() + 60000; // 60000 millis == 1 minute
        long nextMinAtZero = next - (next % 60000);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, nextMinAtZero, mPendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null) {
            mBluetoothService.stop();
        }
        // Unregister broadcast listeners
        unregisterReceiver(mReceiver);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, TimeUpdateReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        mAlarmManager.cancel(mPendingIntent);
    }

    // for alarm service
    public void sendTime() {
        String currentTime = time.format(new Date());
        if (currentTime.charAt(0) == '0')
            currentTime = ' ' + currentTime.substring(1);
        sendMessage(TimeFragment.label + currentTime + TimeFragment.label);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBluetoothService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth chat services
                mBluetoothService.start();
            }
        }
    }

    public void sendMessage(String message) {
        if (mBluetoothService == null) return;

        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBluetoothService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    private void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setSubtitle(subTitle);
    }

    public int getCurrentFragment() {
        return currentFragment;
    }

    private static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
            if (mTag.equals("time")) currentFragment = CURRENT_TIME;
            else if (mTag.equals("echo")) currentFragment = CURRENT_ECHO;
            else if (mTag.equals("query")) currentFragment = CURRENT_QUERY;
            else currentFragment = -1;
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Do nothing
        }
    }

    // Gets information back from BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to));
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            setStatus(getString(R.string.title_connecting));
                            break;
                        case BluetoothService.STATE_NONE:
                            setStatus(getString(R.string.title_not_connected));
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    int currentFragment = getCurrentFragment();
                    if (currentFragment == CURRENT_TIME) {
                        if (writeMessage.equals(Constants.NULLS)) {
                            TimeFragment myFragment = (TimeFragment)getSupportFragmentManager().findFragmentByTag(
                                    "time");
                            if (myFragment != null) {
                                myFragment.sendTime();
                            }
                        }
                    } else if (currentFragment == CURRENT_ECHO) {
                        EchoFragment myFragment = (EchoFragment)getSupportFragmentManager().findFragmentByTag(
                                "echo");
                        myFragment.mConversationArrayAdapter.add(writeMessage.substring(
                                Constants.CODE_LEN, writeMessage.length()-Constants.CODE_LEN));
                    } else if (currentFragment == CURRENT_QUERY) {
                        /*
                        QueryFragment myFragment = (QueryFragment)getSupportFragmentManager().findFragmentByTag(
                                "query");
                        myFragment.mConversationArrayAdapter.add(writeMessage.substring(
                                Constants.CODE_LEN, writeMessage.length()-Constants.CODE_LEN));*/
                    }
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    // Do something with readMessage here
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Intent enableBtIntent1 = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent1, REQUEST_ENABLE_BT);
                        break;
                }
            }
        }
    };
}



