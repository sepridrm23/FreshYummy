package com.freshyummy.android;

/**
 * Created by afi on 17/03/18.
 */

public class Product {

    private String iddetailproduk, namaproduk, kategori,
            deskripsi, satuan, harga, gambar, discount, statusketersediaan;

    public Product(String iddetailproduk, String namaproduk, String kategori, String deskripsi, String satuan, String harga, String gambar, String discount, String statusketersediaan) {
        this.iddetailproduk = iddetailproduk;
        this.namaproduk = namaproduk;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.satuan = satuan;
        this.harga = harga;
        this.gambar = gambar;
        this.discount = discount;
        this.statusketersediaan = statusketersediaan;
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

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
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

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
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
