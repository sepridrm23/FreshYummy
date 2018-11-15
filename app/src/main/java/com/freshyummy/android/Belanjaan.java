package com.freshyummy.android;

/**
 * Created by afi on 18/03/18.
 */

public class Belanjaan {
    String idtransaksi, iddetailtransaksi, statustransaksi, namaproduk, jumlahbeli, hargabeli, satuan, gambar,
            ongkir, poinkeluar, poinmasuk, totalbayar, jumlahproduk, tanggaltransaksi, tanggalpengantaran, tanggalpenerimaan,
            jenisbayar, namapenerima, penerimapengantaran, alamatpengantaran, telepon, keteranganalamat, keteranganbarang,
            namapromo, jenispromo, nominal, namabank, norekening, namarekening, jumlahtransfer, gambartransfer, tanggaltransfer;

    public Belanjaan(String idtransaksi, String iddetailtransaksi, String statustransaksi, String namaproduk, String jumlahbeli,
                     String hargabeli, String satuan, String gambar, String ongkir, String poinkeluar, String totalbayar,
                     String jumlahproduk, String tanggaltransaksi, String tanggalpengantaran, String jenisbayar, String namapenerima,
                     String tanggalpenerimaan, String penerimapengantaran, String alamatpengantaran, String telepon,
                     String keteranganalamat, String keteranganbarang, String namapromo, String nominal, String poinmasuk,
                     String jenispromo, String namabank, String norekening, String namarekening, String jumlahtransfer,
                     String gambartransfer, String tanggaltransfer){
        this.iddetailtransaksi = iddetailtransaksi;
        this.idtransaksi = idtransaksi;
        this.statustransaksi = statustransaksi;
        this.namaproduk = namaproduk;
        this.jumlahbeli = jumlahbeli;
        this.hargabeli = hargabeli;
        this.gambar = gambar;
        this.ongkir = ongkir;
        this.poinkeluar= poinkeluar;
        this.totalbayar = totalbayar;
        this.jumlahproduk = jumlahproduk;
        this.satuan = satuan;
        this.tanggaltransaksi = tanggaltransaksi;
        this.tanggalpengantaran = tanggalpengantaran;
        this.jenisbayar =jenisbayar;
        this.namapenerima = namapenerima;
        this.tanggalpenerimaan = tanggalpenerimaan;
        this.penerimapengantaran = penerimapengantaran;
        this.alamatpengantaran = alamatpengantaran;
        this.telepon = telepon;
        this.keteranganalamat = keteranganalamat;
        this.keteranganbarang = keteranganbarang;
        this.namapromo = namapromo;
        this.jenispromo = jenispromo;
        this.nominal = nominal;
        this.poinmasuk = poinmasuk;
        this.namabank = namabank;
        this.norekening = norekening;
        this.namarekening = namarekening;
        this.jumlahtransfer = jumlahtransfer;
        this.gambartransfer = gambartransfer;
        this.tanggaltransfer = tanggaltransfer;
    }

    public String getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(String idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public String getIddetailtransaksi() {
        return iddetailtransaksi;
    }

    public void setIddetailtransaksi(String iddetailtransaksi) {
        this.iddetailtransaksi = iddetailtransaksi;
    }

    public String getStatustransaksi() {
        return statustransaksi;
    }

    public void setStatustransaksi(String statustransaksi) {
        this.statustransaksi = statustransaksi;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public void setNamaproduk(String namaproduk) {
        this.namaproduk = namaproduk;
    }

    public String getJumlahbeli() {
        return jumlahbeli;
    }

    public void setJumlahbeli(String jumlahbeli) {
        this.jumlahbeli = jumlahbeli;
    }

    public String getHargabeli() {
        return hargabeli;
    }

    public void setHargabeli(String hargabeli) {
        this.hargabeli = hargabeli;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
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

    public String getTotalbayar() {
        return totalbayar;
    }

    public void setTotalbayar(String totalbayar) {
        this.totalbayar = totalbayar;
    }

    public String getJumlahproduk() {
        return jumlahproduk;
    }

    public void setJumlahproduk(String jumlahproduk) {
        this.jumlahproduk = jumlahproduk;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getTanggaltransaksi() {
        return tanggaltransaksi;
    }

    public void setTanggaltransaksi(String tanggaltransaksi) {
        this.tanggaltransaksi = tanggaltransaksi;
    }

    public String getJenisbayar() {
        return jenisbayar;
    }

    public void setJenisbayar(String jenisbayar) {
        this.jenisbayar = jenisbayar;
    }

    public String getTanggalpengantaran() {
        return tanggalpengantaran;
    }

    public void setTanggalpengantaran(String tanggalpengantaran) {
        this.tanggalpengantaran = tanggalpengantaran;
    }

    public String getNamapenerima() {
        return namapenerima;
    }

    public void setNamapenerima(String namapenerima) {
        this.namapenerima = namapenerima;
    }

    public String getTanggalpenerimaan() {
        return tanggalpenerimaan;
    }

    public void setTanggalpenerimaan(String tanggalpenerimaan) {
        this.tanggalpenerimaan = tanggalpenerimaan;
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

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
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

    public String getNamapromo() {
        return namapromo;
    }

    public void setNamapromo(String namapromo) {
        this.namapromo = namapromo;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getPoinmasuk() {
        return poinmasuk;
    }

    public void setPoinmasuk(String poinmasuk) {
        this.poinmasuk = poinmasuk;
    }

    public String getJenispromo() {
        return jenispromo;
    }

    public void setJenispromo(String jenispromo) {
        this.jenispromo = jenispromo;
    }

    public String getNamabank() {
        return namabank;
    }

    public void setNamabank(String namabank) {
        this.namabank = namabank;
    }

    public String getNorekening() {
        return norekening;
    }

    public void setNorekening(String norekening) {
        this.norekening = norekening;
    }

    public String getNamarekening() {
        return namarekening;
    }

    public void setNamarekening(String namarekening) {
        this.namarekening = namarekening;
    }

    public String getJumlahtransfer() {
        return jumlahtransfer;
    }

    public void setJumlahtransfer(String jumlahtransfer) {
        this.jumlahtransfer = jumlahtransfer;
    }

    public String getGambartransfer() {
        return gambartransfer;
    }

    public void setGambartransfer(String gambartransfer) {
        this.gambartransfer = gambartransfer;
    }

    public String getTanggaltransfer() {
        return tanggaltransfer;
    }

    public void setTanggaltransfer(String tanggaltransfer) {
        this.tanggaltransfer = tanggaltransfer;
    }
}
