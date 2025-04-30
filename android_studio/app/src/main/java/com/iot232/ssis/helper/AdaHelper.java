package com.iot232.ssis.helper;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdaHelper {
    private String feedKey;
    private OnTaskCompleted listener;
    private String username, password;

    public AdaHelper(String feedKey, OnTaskCompleted listener, String username, String password) {
        this.feedKey = feedKey;
        this.listener = listener;
        this.username = username;
        this.password = password;
    }

    public void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://io.adafruit.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AdafruitApi apiService = retrofit.create(AdafruitApi.class);
        Call<List<DataEntry>> call = apiService.getDataEntry(username, feedKey, password);

        call.enqueue(new Callback<List<DataEntry>>() {
            @Override
            public void onResponse(@NonNull Call<List<DataEntry>> call, @NonNull Response<List<DataEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onTaskCompleted(response.body());
                } else {
                    listener.onTaskFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DataEntry>> call, @NonNull Throwable t) {
                listener.onTaskFailed();
            }
        });
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(List<DataEntry> dataEntries);
        void onTaskFailed();
    }
}