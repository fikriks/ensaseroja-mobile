package com.algoritma.melani_AE6.model;


import java.util.List;

public class Response {
    private int code;
    private String message;
    private List<DataRes> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataRes> getData() {
        return data;
    }

    public void setData(List<DataRes> data) {
        this.data = data;
    }
}
