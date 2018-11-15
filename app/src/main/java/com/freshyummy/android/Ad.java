package com.freshyummy.android;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marhadi Wijaya on 8/20/2017.
 */

public class Ad{
    private String idiklan, juduliklan, gambar;

    public Ad(String idiklan, String juduliklan, String gambar) {
        this.idiklan = idiklan;
        this.juduliklan = juduliklan;
        this.gambar = gambar;
    }

    public String getIdiklan() {
        return idiklan;
    }

    public void setIdiklan(String idiklan) {
        this.idiklan = idiklan;
    }

    public String getJuduliklan() {
        return juduliklan;
    }

    public void setJuduliklan(String juduliklan) {
        this.juduliklan = juduliklan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
