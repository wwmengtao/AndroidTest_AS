package com.example.protoui.travelmode.route.uber.data;


import com.example.protoui.travelmode.route.uber.data.objects.EtaEstimateResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UberApi {

    String API_ROOT = "https://api.uber.com";


    @GET("/v1.2/estimates/time")
    Call<EtaEstimateResponse> getEtas(@Query("start_latitude") Double lat, @Query("start_longitude") Double lng, @Query("product_id") String rideType);

}
