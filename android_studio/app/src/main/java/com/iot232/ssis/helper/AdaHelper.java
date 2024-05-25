package com.iot232.ssis.helper;

import android.os.AsyncTask;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdaHelper extends AsyncTask<Void, Void, String> {
    public String feedKey;
    public OnTaskCompleted listener;
    public String username, password;

    public AdaHelper(String feedKey, OnTaskCompleted listener, String username, String password) {
        this.feedKey = feedKey;
        this.listener = listener;
        this.username = username;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://io.adafruit.com/api/v2/" + username + "/feeds/" + feedKey + "/data") // Use dynamic feed key
                .header("X-AIO-Key", password)
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
        if (result != null) listener.onTaskCompleted(result);
        else listener.onTaskFailed();
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
        void onTaskFailed();
    }
}
