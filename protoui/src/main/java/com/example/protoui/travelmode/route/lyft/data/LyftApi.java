package com.example.protoui.travelmode.route.lyft.data;


import com.example.protoui.travelmode.route.lyft.data.objects.EtaEstimateResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LyftApi {

    String API_ROOT = "https://api.lyft.com";

    /**
    * Pickup ETAs
    * The ETA endpoint lets you know how quickly a Lyft driver can come get you 
       * @param lat Latitude of a location (required)
       * @param lng Longitude of a location (required)
       * @param rideType ID of a ride type (optional)
       * @return Call&lt;EtaEstimateResponse&gt;
    */
    
    @GET("/v1/eta")
    Call<EtaEstimateResponse> getEtas(@Query("lat") Double lat, @Query("lng") Double lng, @Query("product_id") String rideType);

}
