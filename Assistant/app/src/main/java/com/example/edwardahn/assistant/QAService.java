package com.example.edwardahn.assistant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.TimeZone;

/**
 * Created by edwardahn on 7/17/15.
 */
public class QAService {

    protected int timeout = 10000;
    public String text;

    public QAService(int timeout) { this.timeout = timeout; }

    public String getText() { return text; }

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

        try {
            input = URLEncoder.encode(input, "UTF-8");
        } catch (Exception ex) {
        }

        int timeZoneInMinutes = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000 / 60;

        String url = "https://weannie.pannous.com/api?input=" + input
                + "&locale=en"
                + "&timeZone=" + timeZoneInMinutes
                + "&location=" + location
                + "&login=test-user";

        String result = "";

        try {
            URLConnection conn = new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(timeout);
            conn.setConnectTimeout(timeout);
            result = streamToString(conn.getInputStream(), "UTF-8");
        } catch (Exception ex) {
            text = "Error: " + ex.getMessage();
            return;
        }

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

            //TODO: format text so "who is barack obama" isn't 100 lines long
            //TODO: change all Jeannies to new name
            JSONObject actions = firstHandler.getJSONObject("actions");
            if (actions.has("say")) {
                Object obj = actions.get("say");
                if (obj instanceof JSONObject) {
                    JSONObject sObj = (JSONObject) obj;
                    text = sObj.getString("text");
                    JSONArray arr = sObj.getJSONArray("moreText");
                    for (int i = 0; i < arr.length(); i++) {
                        text += " " + arr.getString(i);
                    }
                } else {
                    text = obj.toString();
                }
            }
        } catch (Exception ex) {
            text = "Parsing error: " + ex.getMessage();
            return;
        }
    }
}
