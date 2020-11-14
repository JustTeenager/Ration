package com.ration.qcode.application.utils;

import java.util.ArrayList;

/**
 * Created by deepdev on 13.04.17.
 */

public class Singleton {

    private ArrayList<String> products;
    private ArrayList<String> proteins;
    private ArrayList<String> fats;
    private ArrayList<String> carbohydrates;
    private ArrayList<String> fas;
    private ArrayList<String> kl;
    private ArrayList<String> gr;

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public ArrayList<String> getProteins() {
        return proteins;
    }

    public void setProteins(ArrayList<String> proteins) {
        this.proteins = proteins;
    }

    public ArrayList<String> getFats() {
        return fats;
    }

    public void setFats(ArrayList<String> fats) {
        this.fats = fats;
    }

    public ArrayList<String> getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(ArrayList<String> carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public ArrayList<String> getFas() {
        return fas;
    }

    public void setFas(ArrayList<String> fas) {
        this.fas = fas;
    }

    public ArrayList<String> getKl() {
        return kl;
    }

    public void setKl(ArrayList<String> kl) {
        this.kl = kl;
    }

    public ArrayList<String> getGr() {
        return gr;
    }

    public void setGr(ArrayList<String> gr) {
        this.gr = gr;
    }

    private static Singleton mInstance = null;

    private Singleton(){
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }
}
