package com.iot232.ssis.helper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdafruitApi {
    @GET("api/v2/{username}/feeds/{feed_key}/data")
    Call<List<DataEntry>> getDataEntry(
            @Path("username") String username,
            @Path("feed_key") String feedKey,
            @Header("X-AIO-Key") String apiKey
    );
}