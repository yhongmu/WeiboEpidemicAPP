package com.example.weiboepidemic.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weiboepidemic.R;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class DownloadFormActivity extends AppCompatActivity {
    private TextView table1;
    private TextView table2;
    private TextView table3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_form);

        table1 = findViewById(R.id.table_1);
        SpannableString spannableString = new SpannableString("所有历史新冠热搜数据报表.xlsx");
        // 设置url链接
        URLSpan urlSpan = new URLSpan("http://121.4.83.168:8090/excel/AllHotSearchData");
        // 一参：url对象； 二参三参：url生效的字符起始位置； 四参：模式
        spannableString.setSpan(urlSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置textView可以点击链接进行跳转（不设置的话点击无反应）
        table1.setMovementMethod(new LinkMovementMethod());
        //把可扩展字符串设置到textView
        table1.setText(spannableString);
        table1.setTextColor(getResources().getColor(R.color.bree));


        table2 = findViewById(R.id.table_2);
        SpannableString spannableString2 = new SpannableString("每月的新冠热搜分析报表.xlsx");
        // 设置url链接
        URLSpan urlSpan2 = new URLSpan("http://121.4.83.168:8090/excel/MonthHotSearchData");
        // 一参：url对象； 二参三参：url生效的字符起始位置； 四参：模式
        spannableString2.setSpan(urlSpan2, 0, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置textView可以点击链接进行跳转（不设置的话点击无反应）
        table2.setMovementMethod(new LinkMovementMethod());
        //把可扩展字符串设置到textView
        table2.setText(spannableString2);
        table2.setTextColor(getResources().getColor(R.color.bree));

        table3 = findViewById(R.id.table_3);
        SpannableString spannableString3 = new SpannableString("热搜关键词出现频率排序报表.xlsx");
        // 设置url链接
        URLSpan urlSpan3 = new URLSpan("http://121.4.83.168:8090/excel/HotSearchKeyword");
        // 一参：url对象； 二参三参：url生效的字符起始位置； 四参：模式
        spannableString3.setSpan(urlSpan3, 0, spannableString3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置textView可以点击链接进行跳转（不设置的话点击无反应）
        table3.setMovementMethod(new LinkMovementMethod());
        //把可扩展字符串设置到textView
        table3.setText(spannableString3);
        table3.setTextColor(getResources().getColor(R.color.bree));


    }
}
