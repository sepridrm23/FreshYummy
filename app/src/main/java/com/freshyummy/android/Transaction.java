package com.freshyummy.android;

/**
 * Created by afi on 18/03/18.
 */

public class Transaction {
    private String idtransaksi, idpelanggan, penerimapengantaran, alamatpengantaran, latitude, longitude, keteranganalamat, keteranganbarang, statustransaksi, statusbayar, jenisbayar, buktibayar, namapenerima, jenispenerima, telepon, gambar, tanggaltransaksi, tanggalpengantaran, tanggalpembayaran, tanggalselesai, iddriver, ongkir, poinkeluar, poinmasuk;

    public Transaction(String idtransaksi, String idpelanggan, String penerimapengantaran, String alamatpengantaran, String latitude, String longitude, String keteranganalamat, String keteranganbarang, String statustransaksi, String statusbayar, String jenisbayar, String buktibayar, String namapenerima, String jenispenerima, String telepon, String gambar, String tanggaltransaksi, String tanggalpengantaran, String tanggalpembayaran, String tanggalselesai, String iddriver, String ongkir, String poinkeluar, String poinmasuk) {
        this.idtransaksi = idtransaksi;
        this.idpelanggan = idpelanggan;
        this.penerimapengantaran = penerimapengantaran;
        this.alamatpengantaran = alamatpengantaran;
        this.latitude = latitude;
        this.longitude = longitude;
        this.keteranganalamat = keteranganalamat;
        this.keteranganbarang = keteranganbarang;
        this.statustransaksi = statustransaksi;
        this.statusbayar = statusbayar;
        this.jenisbayar = jenisbayar;
        this.buktibayar = buktibayar;
        this.namapenerima = namapenerima;
        this.jenispenerima = jenispenerima;
        this.telepon = telepon;
        this.gambar = gambar;
        this.tanggaltransaksi = tanggaltransaksi;
        this.tanggalpengantaran = tanggalpengantaran;
        this.tanggalpembayaran = tanggalpembayaran;
        this.tanggalselesai = tanggalselesai;
        this.iddriver = iddriver;
        this.ongkir = ongkir;
        this.poinkeluar = poinkeluar;
        this.poinmasuk = poinmasuk;
    }

    public String getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(String idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public String getIdpelanggan() {
        return idpelanggan;
    }

    public void setIdpelanggan(String idpelanggan) {
        this.idpelanggan = idpelanggan;
    }

    public String getPenerimapengantaran() {
        return penerimapengantaran;
    }

    public void setPenerimapengantaran(String penerimapengantaran) {
        this.penerimapengantaran = penerimapengantaran;
    }

    public String getAlamatpengantaran() {
        return alamatpengantaran;
    }

    public void setAlamatpengantaran(String alamatpengantaran) {
        this.alamatpengantaran = alamatpengantaran;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKeteranganalamat() {
        return keteranganalamat;
    }

    public void setKeteranganalamat(String keteranganalamat) {
        this.keteranganalamat = keteranganalamat;
    }

    public String getKeteranganbarang() {
        return keteranganbarang;
    }

    public void setKeteranganbarang(String keteranganbarang) {
        this.keteranganbarang = keteranganbarang;
    }

    public String getStatustransaksi() {
        return statustransaksi;
    }

    public void setStatustransaksi(String statustransaksi) {
        this.statustransaksi = statustransaksi;
    }

    public String getStatusbayar() {
        return statusbayar;
    }

    public void setStatusbayar(String statusbayar) {
        this.statusbayar = statusbayar;
    }

    public String getJenisbayar() {
        return jenisbayar;
    }

    public void setJenisbayar(String jenisbayar) {
        this.jenisbayar = jenisbayar;
    }

    public String getBuktibayar() {
        return buktibayar;
    }

    public void setBuktibayar(String buktibayar) {
        this.buktibayar = buktibayar;
    }

    public String getNamapenerima() {
        return namapenerima;
    }

    public void setNamapenerima(String namapenerima) {
        this.namapenerima = namapenerima;
    }

    public String getJenispenerima() {
        return jenispenerima;
    }

    public void setJenispenerima(String jenispenerima) {
        this.jenispenerima = jenispenerima;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getTanggaltransaksi() {
        return tanggaltransaksi;
    }

    public void setTanggaltransaksi(String tanggaltransaksi) {
        this.tanggaltransaksi = tanggaltransaksi;
    }

    public String getTanggalpengantaran() {
        return tanggalpengantaran;
    }

    public void setTanggalpengantaran(String tanggalpengantaran) {
        this.tanggalpengantaran = tanggalpengantaran;
    }

    public String getTanggalpembayaran() {
        return tanggalpembayaran;
    }

    public void setTanggalpembayaran(String tanggalpembayaran) {
        this.tanggalpembayaran = tanggalpembayaran;
    }

    public String getTanggalselesai() {
        return tanggalselesai;
    }

    public void setTanggalselesai(String tanggalselesai) {
        this.tanggalselesai = tanggalselesai;
    }

    public String getIddriver() {
        return iddriver;
    }

    public void setIddriver(String iddriver) {
        this.iddriver = iddriver;
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getPoinkeluar() {
        return poinkeluar;
    }

    public void setPoinkeluar(String poinkeluar) {
        this.poinkeluar = poinkeluar;
    }

    public String getPoinmasuk() {
        return poinmasuk;
    }

    public void setPoinmasuk(String poinmasuk) {
        this.poinmasuk = poinmasuk;
    }
}
