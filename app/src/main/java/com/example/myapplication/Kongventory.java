package com.example.myapplication;

public class Kongventory {
    private String beans_name;
    private String beans_price;
    private int benas_img;

    public Kongventory(String beans_name, String beans_price, int benas_img) {
        this.beans_name = beans_name;
        this.beans_price = beans_price;
        this.benas_img = benas_img;
    }

    public String getBeans_name() {
        return beans_name;
    }

    public void setBeans_name(String beans_name) {
        this.beans_name = beans_name;
    }

    public String getBeans_price() {
        return beans_price;
    }

    public void setBeans_price(String beans_price) {
        this.beans_price = beans_price;
    }

    public int getBenas_img() {
        return benas_img;
    }

    public void setBenas_img(int benas_img) {
        this.benas_img = benas_img;
    }
}
