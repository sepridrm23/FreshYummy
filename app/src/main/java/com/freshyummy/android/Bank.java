package com.freshyummy.android;

/**
 * Created by Marhadi Wijaya on 9/15/2017.
 */

public class Bank {
    private String idbank, namabank, norekening, namarekening, gambar;

    public Bank(String idbank, String namabank, String norekening, String namarekening, String gambar) {
        this.idbank = idbank;
        this.namabank = namabank;
        this.norekening = norekening;
        this.namarekening = namarekening;
        this.gambar = gambar;
    }

    public String getIdbank() {
        return idbank;
    }

    public String getGambar() {
        return gambar;
    }

    public String getNamabank() {
        return namabank;
    }

    public String getNorekening() {
        return norekening;
    }

    public String getNamarekening() {
        return namarekening;
    }

}
