package com.example.weather.activity.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServerResponse implements Serializable {


    @SerializedName("list")
    private java.util.List<List> list = null;

    public ServerResponse() {
    }


    public ServerResponse( java.util.List<List> list) {
        super();

        this.list = list;
    }

    public java.util.List<List> getList() {
        return list;
    }

    public void setList(java.util.List<List> list) {
        this.list = list;
    }
}
