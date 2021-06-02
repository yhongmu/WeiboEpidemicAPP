package com.example.weiboepidemic.entity;

import com.google.gson.annotations.SerializedName;

public class HotSearchEntity {
    @SerializedName("search_key")
    public String searchKey;

    public int heat;

    @SerializedName("real_url")
    public String realURL;

    @SerializedName("create_time")
    public String createTime;

    public HotSearchEntity(String searchKey, int heat, String realURL, String createTime) {
        this.searchKey = searchKey;
        this.heat = heat;
        this.realURL = realURL;
        this.createTime = createTime;
    }
}

