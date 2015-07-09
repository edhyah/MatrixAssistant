package com.example.edwardahn.assistant;

/**
 * Created by edwardahn on 6/30/15.
 */
public interface Constants {
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Codes
    public static final String NULLS = "/NULL/";
    public static final String TIMES = "_TIME_";
    public static final String ECHOS = "_ECHO_";
    public static final String QUERYS = "_QUER_";
    public static final int CODE_LEN = 6;
}
