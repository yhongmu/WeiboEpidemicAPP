package com.example.weiboepidemic.ui.today;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weiboepidemic.MyApplication;
import com.example.weiboepidemic.R;
import com.example.weiboepidemic.adapter.TodayRecyclerAdapter;
import com.example.weiboepidemic.entity.HotSearchEntity;
import com.example.weiboepidemic.entity.DecJsonEntity;
import com.example.weiboepidemic.ui.LoadDialog;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TodayFragment extends Fragment {
    private static final String TODAY_HOT_SEARCH_URL = "http://121.4.83.168:8090/weibo_epidemic/today_search";
    private RecyclerView mRv;
    private LoadDialog loadDialog;
    private SwipeRefreshLayout refreshLayout;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    TodayRecyclerAdapter adapter = new TodayRecyclerAdapter(
                            getContext(), (List<HotSearchEntity>)msg.obj);
                    mRv.setAdapter(adapter);
                    Toast.makeText(getContext(),
                            "实时新冠疫情热搜请求成功", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getContext(),
                            (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(getContext(),
                            "实时新冠疫情热搜请求失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            loadDialog.dismiss();
            refreshLayout.setRefreshing(false);
            return false;
        }
    });

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        initView(root);
        refresh();
        return root;
    }

    public void initView(View root) {
        loadDialog = new LoadDialog(getContext());
        mRv = root.findViewById(R.id.today_list);
        refreshLayout = root.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadDialog.show();
                requestTodayHotSearch();
            }
        });
        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
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
        List<HotSearchEntity> mData = ((MyApplication)getActivity().getApplication()).getmData();
        if (mData == null) {
            loadDialog.show();
            requestTodayHotSearch();
        } else {
            TodayRecyclerAdapter adapter =
                    new TodayRecyclerAdapter(getContext(), mData);
            mRv.setAdapter(adapter);
        }
    }

    public void requestTodayHotSearch() {
        Request todayRequest = new Request.Builder().url(TODAY_HOT_SEARCH_URL).build();
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
                ((MyApplication)getActivity().getApplication()).setmData(decJsonEntity.data);
                Message message = mHandler.obtainMessage();
                if(decJsonEntity.code == 0) {
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