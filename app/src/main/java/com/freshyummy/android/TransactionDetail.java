package com.freshyummy.android;

/**
 * Created by afi on 18/03/18.
 */

public class TransactionDetail {
    private String iddetailtransaksi, idtransaksi, idproduk, namaproduk, hargaproduk, satuan, jumlahbeli, gambar;

    public TransactionDetail(String iddetailtransaksi, String idtransaksi, String idproduk, String namaproduk, String hargaproduk, String satuan, String jumlahbeli, String gambar) {
        this.iddetailtransaksi = iddetailtransaksi;
        this.idtransaksi = idtransaksi;
        this.idproduk = idproduk;
        this.namaproduk = namaproduk;
        this.hargaproduk = hargaproduk;
        this.satuan = satuan;
        this.jumlahbeli = jumlahbeli;
        this.gambar = gambar;
    }

    public String getIddetailtransaksi() {
        return iddetailtransaksi;
    }

    public void setIddetailtransaksi(String iddetailtransaksi) {
        this.iddetailtransaksi = iddetailtransaksi;
    }

    public String getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(String idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public String getIdproduk() {
        return idproduk;
    }

    public void setIdproduk(String idproduk) {
        this.idproduk = idproduk;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public void setNamaproduk(String namaproduk) {
        this.namaproduk = namaproduk;
    }

    public String getHargaproduk() {
        return hargaproduk;
    }

    public void setHargaproduk(String hargaproduk) {
        this.hargaproduk = hargaproduk;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getJumlahbeli() {
        return jumlahbeli;
    }

    public void setJumlahbeli(String jumlahbeli) {
        this.jumlahbeli = jumlahbeli;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
