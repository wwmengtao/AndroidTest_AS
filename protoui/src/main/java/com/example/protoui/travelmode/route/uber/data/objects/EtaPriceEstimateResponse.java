package com.example.protoui.travelmode.route.uber.data.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EtaPriceEstimateResponse {

    @SerializedName("prices")
    public final List<EtaPrice> prices;

    public EtaPriceEstimateResponse(List<EtaPrice> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EtaPriceEstimateResponse {\n");

        sb.append("  prices: ").append(prices).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
