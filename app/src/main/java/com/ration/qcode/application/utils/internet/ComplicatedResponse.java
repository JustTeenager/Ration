package com.ration.qcode.application.utils.internet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplicatedResponse {
    @SerializedName("Name")
    @Expose
    private String name;
    @Expose
    private String product;
    @SerializedName("Jiry")
    @Expose
    private String jiry;
    @SerializedName("Belki")
    @Expose
    private String belki;
    @SerializedName("Uglevod")
    @Expose
    private String uglevod;
    @SerializedName("FA")
    @Expose
    private String fa;
    @SerializedName("kkal")
    @Expose
    private String kl;
    @Expose
    private String gram;
    @SerializedName("complicated")
    @Expose
    private String complicated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getJiry() {
        return jiry;
    }

    public void setJiry(String jiry) {
        this.jiry = jiry;
    }

    public String getBelki() {
        return belki;
    }

    public void setBelki(String belki) {
        this.belki = belki;
    }

    public String getUglevod() {
        return uglevod;
    }

    public void setUglevod(String uglevod) {
        this.uglevod = uglevod;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getKl() {
        return kl;
    }

    public void setKl(String kl) {
        this.kl = kl;
    }

    public String getGram() {
        return gram;
    }

    public void setGram(String gram) {
        this.gram = gram;
    }

    public String getComplicated() {
        return complicated;
    }

    public void setComplicated(String complicated) {
        this.complicated = complicated;
    }
}
