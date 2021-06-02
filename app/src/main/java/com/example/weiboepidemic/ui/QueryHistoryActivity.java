package com.example.weiboepidemic.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weiboepidemic.MyApplication;
import com.example.weiboepidemic.R;
import com.example.weiboepidemic.adapter.QueryRecyclerAdapter;
import com.example.weiboepidemic.adapter.TodayRecyclerAdapter;
import com.example.weiboepidemic.entity.HotSearchEntity;
import com.example.weiboepidemic.entity.LocalHotSearchEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryHistoryActivity extends AppCompatActivity {
    private RecyclerView mRv;
    private LoadDialog loadDialog;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    QueryRecyclerAdapter adapter = new QueryRecyclerAdapter(getApplicationContext(), (List<LocalHotSearchEntity>)msg.obj);
                    mRv.setAdapter(adapter);
                    Toast.makeText(getApplicationContext(),
                            "获取本地的浏览历史数据成功！", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(),
                            "获取本地的浏览历史数据失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
            loadDialog.dismiss();
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_history);
        initView();
        refresh();
    }

    public void initView() {
        loadDialog = new LoadDialog(this);
        mRv = findViewById(R.id.query_list);
        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);

        mRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(10, 0, 10, 10);
            }

        });
    }

    public void refresh() {
        loadDialog.show();
        SQLiteDatabase db = ((MyApplication)getApplication()).dbHelper.getWritableDatabase();
        Cursor cursor = db.query("HistoryHotSearch", null, null,
                null, null, null, null);
        List<LocalHotSearchEntity> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                LocalHotSearchEntity entity = new LocalHotSearchEntity();
                entity.searchKey = cursor.getString(cursor.getColumnIndex("search_key"));
                entity.realURL = cursor.getString(cursor.getColumnIndex("real_url"));
                entity.createTime = cursor.getString(cursor.getColumnIndex("create_time"));
                entity.year = cursor.getInt(cursor.getColumnIndex("year"));
                entity.month = cursor.getInt(cursor.getColumnIndex("month"));
                entity.day = cursor.getInt(cursor.getColumnIndex("day"));
                list.add(entity);
            } while (cursor.moveToNext());
        }
        if (list.size() == 0) {
            mHandler.sendEmptyMessage(0);
        } else {
            Collections.sort(list);
            Message message = mHandler.obtainMessage();
            message.what = 1;
            message.obj = list;
            mHandler.sendMessage(message);
        }
    }
}
