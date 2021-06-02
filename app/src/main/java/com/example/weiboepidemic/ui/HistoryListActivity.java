package com.example.weiboepidemic.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weiboepidemic.R;
import com.example.weiboepidemic.adapter.HistoryRecyclerAdapter;
import com.example.weiboepidemic.adapter.TodayRecyclerAdapter;
import com.example.weiboepidemic.entity.HotSearchEntity;
import com.example.weiboepidemic.entity.DecJsonEntity;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryListActivity extends AppCompatActivity {
    private static final String HISTORY_HOT_SEARCH_URL = "http://121.4.83.168:8090/weibo_epidemic/past_search";
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private TextView dateLimitText;
    private RecyclerView mRv;
    private LoadDialog loadDialog;
    private String heat, startDate, endDate;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    HistoryRecyclerAdapter adapter = new HistoryRecyclerAdapter(getApplicationContext(), (List<HotSearchEntity>)msg.obj);
                    mRv.setAdapter(adapter);
                    Toast.makeText(getApplicationContext(),
                            "历史新冠疫情热搜请求成功", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(),
                            (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(),
                            "历史新冠疫情热搜请求失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            loadDialog.dismiss();
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        heat = bundle.getString("heat");
        startDate = bundle.getString("start_date");
        endDate = bundle.getString("end_date");
        initView();
        refresh();
    }

    public void initView() {
        loadDialog = new LoadDialog(this);
        dateLimitText = findViewById(R.id.date_limit);
        dateLimitText.setText(startDate + " 至 " + endDate);
        mRv = findViewById(R.id.history_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRv.setLayoutManager(layoutManager);
        mRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(40, 0, 30, 20);
            }

        });
    }

    public void refresh() {
        loadDialog.show();
        requestHistoryHotSearch();
    }

    public void requestHistoryHotSearch() {
        RequestBody requestBody = new FormBody.Builder()
                .add("start_time", startDate)
                .add("end_time", endDate)
                .add("heat", heat)
                .build();
        Request todayRequest = new Request.Builder()
                .url(HISTORY_HOT_SEARCH_URL)
                .post(requestBody)
                .build();
        Call todayCall = client.newCall(todayRequest);
        todayCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.w("homa", e.getMessage());
                mHandler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resultStr = Objects.requireNonNull(response.body()).string();
                Gson gson = new Gson();
                DecJsonEntity decJsonEntity = gson.fromJson(resultStr, DecJsonEntity.class);
                Message message = mHandler.obtainMessage();
                if (decJsonEntity.data != null) {
                    message.what = 1;
                    message.obj = decJsonEntity.data;
                    mHandler.sendMessage(message);
                } else {
                    message.what = 0;
                    message.obj = decJsonEntity.message;
                    mHandler.sendMessage(message);
                }
            }
        });

    }
}
