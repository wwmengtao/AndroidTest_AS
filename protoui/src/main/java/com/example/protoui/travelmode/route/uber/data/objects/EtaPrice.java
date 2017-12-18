package com.example.protoui.travelmode.route.uber.data.objects;

/**
 * Created by mengtao1 on 2017/12/15.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Estimated Price of Arrival
 **/
public class EtaPrice {

    @SerializedName("product_id")
    public String product_id;

    @SerializedName("currency_code")
    public String currency_code;

    @SerializedName("display_name")
    public String display_name;

    @SerializedName("localized_display_name")
    public String localized_display_name;

    @SerializedName("estimate")
    public String estimate;

    @SerializedName("minimum")
    public Integer minimum;

    @SerializedName("low_estimate")
    public Integer low_estimate;

    @SerializedName("high_estimate")
    public Integer high_estimate;

    @SerializedName("surge_multiplier")
    public Float surge_multiplier;

    @SerializedName("duration")
    public Integer duration;

    @SerializedName("distance")
    public Float distance;

    public void setProduct_id(String product_id){
        this.product_id = product_id;
    }

    public void setCurrency_code(String currency_code){
        this.currency_code = currency_code;
    }

    public void setDisplay_name(String display_name){
        this.display_name = display_name;
    }

    public void setLocalized_display_name(String localized_display_name){
        this.localized_display_name = localized_display_name;
    }

    public void setEstimate(String estimate){
        this.estimate = estimate;
    }

    public void setMinimum(Integer minimum){
        this.minimum = minimum;
    }

    public void setLow_estimate(Integer low_estimate){
        this.low_estimate = low_estimate;
    }

    public void setHigh_estimate(Integer high_estimate){
        this.high_estimate = high_estimate;
    }

    public void setSurgemultiplier(Float surge_multiplier){
        this.surge_multiplier = surge_multiplier;
    }

    public void setDuration(Integer duration){
        this.duration = duration;
    }

    public void setDistance(Float distance){
        this.distance = distance;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EtaPrice {\n");
        sb.append("  product_id: ").append(product_id).append("\n");
        sb.append("  localized_display_name: ").append(localized_display_name).append("\n");
        sb.append("  display_name: ").append(display_name).append("\n");
        sb.append("  estimate: ").append(estimate).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
