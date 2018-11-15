package com.freshyummy.android;

/**
 * Created by afi on 18/04/18.
 */

public class Promo {
    String idpromo, namapromo, nominal, tanggalberakhir, status, used, jenispromo;

    public Promo(String idpromo, String namapromo, String nominal, String tanggalberakhir, String status, String used, String jenispromo){
        this.idpromo = idpromo;
        this.namapromo = namapromo;
        this.nominal = nominal;
        this.tanggalberakhir = tanggalberakhir;
        this.status = status;
        this.used = used;
        this.jenispromo = jenispromo;
    }

    public String getIdpromo() {
        return idpromo;
    }

    public void setIdpromo(String idpromo) {
        this.idpromo = idpromo;
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

    public String getTanggalberakhir() {
        return tanggalberakhir;
    }

    public void setTanggalberakhir(String tanggalberakhir) {
        this.tanggalberakhir = tanggalberakhir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getJenispromo() {
        return jenispromo;
    }

    public void setJenispromo(String jenispromo) {
        this.jenispromo = jenispromo;
    }
}
