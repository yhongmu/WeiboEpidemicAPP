package com.example.weiboepidemic;

import android.app.Application;

import com.example.weiboepidemic.dao.MyDatabaseHelper;
import com.example.weiboepidemic.entity.HotSearchEntity;

import java.util.List;

public class MyApplication extends Application {
    private List<HotSearchEntity> mData;
    public MyDatabaseHelper dbHelper;

    public List<HotSearchEntity> getmData() {
        return mData;
    }
    public void setmData(List<HotSearchEntity> data) {
        mData = data;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        dbHelper = new MyDatabaseHelper(this, "HistoryHotSearch.db", null, 1);
    }
}
