package com.freshyummy.android;

/**
 * Created by afi on 16/03/18.
 */

public class Cart {
    private String idkeranjang, iddetailproduk, namaproduk, gambar, satuan, harga, jumlahbeli, discount, statusketersediaan;

    public Cart(String idkeranjang, String iddetailproduk, String namaproduk, String gambar, String satuan, String harga, String jumlahbeli, String discount, String statusketersediaan) {
        this.idkeranjang = idkeranjang;
        this.iddetailproduk = iddetailproduk;
        this.namaproduk = namaproduk;
        this.gambar = gambar;
        this.satuan = satuan;
        this.harga = harga;
        this.jumlahbeli = jumlahbeli;
        this.discount = discount;
        this.statusketersediaan = statusketersediaan;
    }

    public String getIdkeranjang() {
        return idkeranjang;
    }

    public void setIdkeranjang(String idkeranjang) {
        this.idkeranjang = idkeranjang;
    }

    public String getIddetailproduk() {
        return iddetailproduk;
    }

    public void setIddetailproduk(String iddetailproduk) {
        this.iddetailproduk = iddetailproduk;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public void setNamaproduk(String namaproduk) {
        this.namaproduk = namaproduk;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlahbeli() {
        return jumlahbeli;
    }

    public void setJumlahbeli(String jumlahbeli) {
        this.jumlahbeli = jumlahbeli;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStatusketersediaan() {
        return statusketersediaan;
    }

    public void setStatusketersediaan(String statusketersediaan) {
        this.statusketersediaan = statusketersediaan;
    }
}
