package com.ration.qcode.application.utils.internet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 13.07.2017.
 *
 * @author Влад.
 */

public class AddProductBody {

    public AddProductBody(){
    }

    @Expose
    private String name;

    @SerializedName("FA")
    @Expose
    private String fa;

    @SerializedName("KKAL")
    @Expose
    private String kkal;

    @SerializedName("Belok")
    @Expose
    private String belok;

    @SerializedName("Uglevod")
    @Expose
    private String uglevod;

    @SerializedName("Jiry")
    @Expose
    private String jiry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getKkal() {
        return kkal;
    }

    public void setKkal(String kkal) {
        this.kkal = kkal;
    }

    public String getBelok() {
        return belok;
    }

    public void setBelok(String belok) {
        this.belok = belok;
    }

    public String getUglevod() {
        return uglevod;
    }

    public void setUglevod(String uglevod) {
        this.uglevod = uglevod;
    }

    public String getJiry() {
        return jiry;
    }

    public void setJiry(String jiry) {
        this.jiry = jiry;
    }
}
