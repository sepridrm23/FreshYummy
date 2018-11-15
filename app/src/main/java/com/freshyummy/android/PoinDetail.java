package com.freshyummy.android;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marhadi Wijaya on 10/11/2017.
 */

public class PoinDetail {
    private String iddetailpoin, idpelanggan, tanggal, nominal, idtransaksi, idpromo, namaproduk, gambar, statustransaksi;

    public PoinDetail(String iddetailpoin, String idpelanggan, String tanggal, String idtransaksi,
                      String idpromo, String namaproduk, String gambar, String nominal, String statustransaksi) {
        this.iddetailpoin = iddetailpoin;
        this.idpelanggan = idpelanggan;
        this.tanggal = tanggal;
        this.nominal = nominal;
        this.idpromo = idpromo;
        this.namaproduk = namaproduk;
        this.gambar = gambar;
        this.idtransaksi = idtransaksi;
        this.statustransaksi = statustransaksi;
    }

    public String getIddetailpoin() {
        return iddetailpoin;
    }

    public String getIdpelanggan() {
        return idpelanggan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNominal() {
        return nominal;
    }

    public String getIdtransaksi() {
        return idtransaksi;
    }

    public String getIdpromo() {
        return idpromo;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public String getGambar() {
        return gambar;
    }

    public String getStatustransaksi() {
        return statustransaksi;
    }
}
