package com.freshyummy.android;

/**
 * Created by afi on 15/03/18.
 */

public class Category {
    private String namakategori;
    private String gambar;

    public Category(String namakategori, String gambar) {
        this.namakategori = namakategori;
        this.gambar = gambar;
    }

    public String getNamakategori() {
        return namakategori;
    }

    public void setNamakategori(String namakategori) {
        this.namakategori = namakategori;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
