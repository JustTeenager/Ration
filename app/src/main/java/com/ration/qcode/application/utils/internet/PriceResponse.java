package com.ration.qcode.application.utils.internet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deepdev on 26.04.17.
 */
public class PriceResponse {

    @SerializedName("price")
    @Expose
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
