package com.algoritma.melani_AE6.model;


import java.util.List;

public class DataRes {
    // Deklarasi variabel untuk menyimpan data produk
    private int id;  // ID produk
    private String kode_produk;  // Kode unik produk
    private String kode; // Kode unik produksi
    private String nama;  // Nama produk
    private String foto;  // Nama file foto produk
    private String komposisi;  // Komposisi bahan produk
    private String no_pirt;  // Nomor PIRT produk
    private String produsen;  // Nama produsen
    private String tgl_produksi;  // Tanggal produksi
    private String tgl_expire;  // Tanggal kadaluarsa

    private String kode_enkripsi;  // Tanggal kadaluarsa

    private String message;  // Message

    private List<DataRes> data;

    // Getter dan Setter untuk setiap field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter dan Setter untuk setiap field
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKode_produk() {
        return kode_produk;
    }

    public String getKode_produksi() {
        return kode;
    }

    public void setCipettext(String kode_enkripsi) {
        this.kode_enkripsi = kode_enkripsi;
    }

    public String getCiphertext() {
        return kode_enkripsi;
    }

    public void setKode_produk(String kode_produk) {
        this.kode_produk = kode_produk;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKomposisi() {
        return komposisi;
    }

    public void setKomposisi(String komposisi) {
        this.komposisi = komposisi;
    }

    public String getNo_pirt() {
        return no_pirt;
    }

    public void setNo_pirt(String no_pirt) {
        this.no_pirt = no_pirt;
    }

    public String getProdusen() {
        return produsen;
    }

    public void setProdusen(String produsen) {
        this.produsen = produsen;
    }

    public String getTgl_produksi() {
        return tgl_produksi;
    }

    public void setTgl_produksi(String tgl_produksi) {
        this.tgl_produksi = tgl_produksi;
    }

    public String getTgl_expire() {
        return tgl_expire;
    }

    public void setTgl_expire(String tgl_expire) {
        this.tgl_expire = tgl_expire;
    }

    // Getter untuk list
    public List<DataRes> getData() {
        return data;
    }

    // Setter untuk list
    public void setData(List<DataRes> data) {
        this.data = data;
    }
}
