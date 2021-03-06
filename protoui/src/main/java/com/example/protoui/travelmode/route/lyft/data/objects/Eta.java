package com.example.protoui.travelmode.route.lyft.data.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Estimated Time of Arrival
 **/
public class Eta {

    @SerializedName("ride_type")
    public final String ride_type;

    @SerializedName("display_name")
    public final String display_name;

    @SerializedName("eta_seconds")
    public final Integer eta_seconds;

    public Eta(String ride_type, String display_name, Integer eta_seconds) {
        this.ride_type = ride_type;
        this.display_name = display_name;
        this.eta_seconds = eta_seconds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Eta {\n");

        sb.append("  ride_type: ").append(ride_type).append("\n");
        sb.append("  display_name: ").append(display_name).append("\n");
        sb.append("  eta_seconds: ").append(eta_seconds).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
