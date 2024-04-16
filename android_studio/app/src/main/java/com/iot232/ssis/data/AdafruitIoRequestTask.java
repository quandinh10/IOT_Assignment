package com.iot232.ssis.data;

import android.os.AsyncTask;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdafruitIoRequestTask extends AsyncTask<Void, Void, String> {

    private static final String ADAFRUIT_IO_URL = "https://io.adafruit.com/api/v2/";
    private static final String IO_KEY = "";
    private static final String USERNAME = "project_IOT_hcmut";
    private String feedKey; // Dynamic feed key

    private OnTaskCompleted listener;

    public AdafruitIoRequestTask(String feedKey, OnTaskCompleted listener) {
        this.feedKey = feedKey;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ADAFRUIT_IO_URL + USERNAME + "/feeds/" + feedKey + "/data") // Use dynamic feed key
                .header("X-AIO-Key", IO_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                // Handle unsuccessful response
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle network error
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            listener.onTaskCompleted(result);
        } else {
            listener.onTaskFailed();
        }
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
        void onTaskFailed();
    }
}
