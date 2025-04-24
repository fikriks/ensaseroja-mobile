package com.algoritma.melani_AE6.model;


import java.util.List;

public class ResponseHome {
    private int code;
    private String message;
    private List<DataResHome> data;

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

    public List<DataResHome> getData() {
        return data;
    }

    public void setData(List<DataResHome> data) {
        this.data = data;
    }
}
