package com.algoritma.melani_AE6.model;

import java.util.List;

public class Response {
    // Kode status response dari API
    private int code;
    // Pesan response dari API
    private String message;
    // List data produk yang diterima dari API
    private List<DataRes> data;

    // Getter untuk kode status
    public int getCode() {
        return code;
    }

    // Setter untuk kode status
    public void setCode(int code) {
        this.code = code;
    }

    // Getter untuk pesan response
    public String getMessage() {
        return message;
    }

    // Setter untuk pesan response
    public void setMessage(String message) {
        this.message = message;
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
