package com.ration.qcode.application.utils.internet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateMenuResponse {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("menu")
    @Expose
    private String menu;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
