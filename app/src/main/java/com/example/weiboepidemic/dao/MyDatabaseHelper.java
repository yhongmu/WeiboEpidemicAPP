package com.example.weiboepidemic.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_HOT_SEARCH = "create table HistoryHotSearch(" +
            "id integer primary key autoincrement, " +
            "search_key text UNIQUE ON CONFLICT REPLACE, " +
            "real_url text, " +
            "create_time text, " +
            "year integer, " +
            "month integer, " +
            "day integer)";
    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HOT_SEARCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists HistoryHotSearch");
        onCreate(db);
    }
}
