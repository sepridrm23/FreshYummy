package com.freshyummy.android;

public class User {
    private String idpelanggan, nama, telepon, email, tanggaldaftar, statusverifikasiemail, poin, kodereferensi, referensipelanggan;

    public User() {
    }

    public User(String idpelanggan, String nama, String telepon, String email, String tanggaldaftar,
                String statusverifikasiemail, String poin, String kodereferensi, String referensipelanggan) {
        this.idpelanggan = idpelanggan;
        this.nama = nama;
        this.telepon = telepon;
        this.email = email;
        this.tanggaldaftar = tanggaldaftar;
        this.statusverifikasiemail = statusverifikasiemail;
        this.poin = poin;
        this.kodereferensi = kodereferensi;
        this.referensipelanggan = referensipelanggan;
    }

    public String getIdpelanggan() {
        return idpelanggan;
    }

    public void setIdpelanggan(String idpelanggan) {
        this.idpelanggan = idpelanggan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTanggaldaftar() {
        return tanggaldaftar;
    }

    public void setTanggaldaftar(String tanggaldaftar) {
        this.tanggaldaftar = tanggaldaftar;
    }

    public String getStatusverifikasiemail() {
        return statusverifikasiemail;
    }

    public void setStatusverifikasiemail(String statusverifikasiemail) {
        this.statusverifikasiemail = statusverifikasiemail;
    }

    public String getPoin() {
        return poin;
    }

    public void setPoin(String poin) {
        this.poin = poin;
    }

    public String getKodereferensi() {
        return kodereferensi;
    }

    public void setKodereferensi(String kodereferensi) {
        this.kodereferensi = kodereferensi;
    }

    public String getReferensipelanggan() {
        return referensipelanggan;
    }

    public void setReferensipelanggan(String referensipelanggan) {
        this.referensipelanggan = referensipelanggan;
    }
}
