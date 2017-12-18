package com.example.protoui.travelmode.route.lyft.data;


import com.example.protoui.travelmode.route.lyft.data.objects.EtaEstimateResponse;
import com.example.protoui.travelmode.route.lyft.data.objects.EtaPriceEstimateResponse;

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
    Call<EtaEstimateResponse> getEtas(@Query("lat") Double lat, @Query("lng") Double lng, @Query("ride_type") String rideType);

    @GET("/v1/cost")
    Call<EtaPriceEstimateResponse> getCosts(@Query("start_lat") Double startLat, @Query("start_lng") Double startLng, @Query("ride_type") String rideType, @Query("end_lat") Double endLat, @Query("end_lng") Double endLng);

}
