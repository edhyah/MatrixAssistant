package com.example.edwardahn.assistant;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.TimeZone;

import opennlp.tools.sentdetect.SentenceDetector;


/**
 * Created by edwardahn on 7/17/15.
 */
public class QAService {

    protected int timeout = 10000;
    public String text;

    public QAService(int timeout) { this.timeout = timeout; }

    public String getText() { return text; }

    private String deleteSentences(String s) {
        //
        return s;
    }

    private String processText(String s) {
        // delete parenthetical
        s = s.replaceAll("\\(.*\\)", "");
        // delete all sentences except the first
        s = deleteSentences(s);
        // NEO not Jeannie
        return s;
    }

    public static String streamToString(InputStream is, String encoding)
            throws IOException {
        try {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream(
                    buffer.length);
            int numRead;
            while ((numRead = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, numRead);
            }
            return outStream.toString(encoding);
        } finally {
            is.close();
        }
    }

    public void runQA(String input, String location) {

        Log.i("", "running QA now" );

        try {
            input = URLEncoder.encode(input, "UTF-8");
        } catch (Exception ex) {
            Log.e("",ex.getMessage());
        }

        int timeZoneInMinutes = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000 / 60;

        String url = "https://weannie.pannous.com/api?input=" + input
                + "&locale=en"
                + "&timeZone=" + timeZoneInMinutes
                + "&location=" + location
                + "&login=test-user";

        String result = "";

        Log.i("", "launching URL connection");

        try {
            URLConnection conn = new URL(url).openConnection();
            Log.i("", "conn instantiated");
            conn.setDoOutput(true);
            Log.i("", "set do output done");
            conn.setReadTimeout(timeout);
            Log.i("", "setreadtimeout done");
            conn.setConnectTimeout(timeout);
            Log.i("", "setconnecttimeout done");
            result = streamToString(conn.getInputStream(), "UTF-8");
            Log.i("", "maybe a null pointer exception");
        } catch (Exception ex) {
            text = "Error: " + ex.getMessage();
            Log.e("",ex.getMessage());
            return;
        }

        Log.i("", "finding answer now");

        try {

            if (result == null || result.length() == 0) {
                text = "Error processing answer";
                return;
            }

            JSONArray outputJson = new JSONObject(result).getJSONArray("output");
            if (outputJson.length() == 0) {
                text = "Sorry, nothing found";
                return;
            }

            JSONObject firstHandler = outputJson.getJSONObject(0);
            if (firstHandler.has("errorMessage") && firstHandler.getString("errorMessage").length() > 0) {
                throw new RuntimeException("Server side error: "
                        + firstHandler.getString("errorMessage"));
            }

            JSONObject actions = firstHandler.getJSONObject("actions");
            if (actions.has("say")) {
                Object obj = actions.get("say");
                if (obj instanceof JSONObject) {
                    JSONObject sObj = (JSONObject) obj;
                    text = sObj.getString("text");
                } else {
                    text = obj.toString();
                }
                text = processText(text);
            }
        } catch (Exception ex) {
            text = "Parsing error: " + ex.getMessage();
            Log.e("",text);
            return;
        }
    }
}
