package com.ration.qcode.application.utils.internet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateResponse {
    @SerializedName("date")
    @Expose
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
