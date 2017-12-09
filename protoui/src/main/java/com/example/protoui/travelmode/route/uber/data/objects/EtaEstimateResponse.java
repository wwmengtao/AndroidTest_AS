package com.example.protoui.travelmode.route.uber.data.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EtaEstimateResponse {

    @SerializedName("times")
    public final List<Eta> times;

    public EtaEstimateResponse(List<Eta> times) {
        this.times = times;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EtaEstimateResponse {\n");

        sb.append("  times: ").append(times).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
