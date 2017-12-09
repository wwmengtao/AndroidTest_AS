package com.example.protoui.travelmode.route.uber.data.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Estimated Time of Arrival
 **/
public class Eta {

    @SerializedName("product_id")
    public final String product_id;

    @SerializedName("localized_display_name")
    public final String localized_display_name;

    @SerializedName("display_name")
    public final String display_name;

    @SerializedName("estimate")
    public final Integer estimate;

    public Eta(String product_id, String localized_display_name, String display_name, Integer estimate) {
        this.product_id = product_id;
        this.localized_display_name = localized_display_name;
        this.display_name = display_name;
        this.estimate = estimate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Eta {\n");
        sb.append("  product_id: ").append(product_id).append("\n");
        sb.append("  localized_display_name: ").append(localized_display_name).append("\n");
        sb.append("  display_name: ").append(display_name).append("\n");
        sb.append("  estimate: ").append(estimate).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
