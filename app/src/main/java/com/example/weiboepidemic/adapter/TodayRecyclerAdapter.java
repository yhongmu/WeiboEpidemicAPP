package com.example.weiboepidemic.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weiboepidemic.MainActivity;
import com.example.weiboepidemic.MyApplication;
import com.example.weiboepidemic.R;
import com.example.weiboepidemic.entity.HotSearchEntity;
import com.example.weiboepidemic.ui.HistoryListActivity;
import com.example.weiboepidemic.ui.WebViewActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodayRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<HotSearchEntity> mData;

    public TodayRecyclerAdapter(Context context, List<HotSearchEntity> data) {
        mContext = context;
        mData = data;
    }

    public static class TodayHolder extends RecyclerView.ViewHolder {
        public TextView numText;
        public TextView keyText;
        public TextView heatText;

        public TodayHolder(@NonNull View itemView) {
            super(itemView);
            numText = itemView.findViewById(R.id.today_num);
            keyText = itemView.findViewById(R.id.today_key);
            heatText = itemView.findViewById(R.id.today_heat);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_today_recycler, parent, false);
        return new TodayHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TodayHolder todayHolder = (TodayHolder) holder;
        todayHolder.numText.setText(String.valueOf(position + 1));
        todayHolder.keyText.setText(mData.get(position).searchKey);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = ((MyApplication)mContext.getApplicationContext()).dbHelper.getWritableDatabase();
                Calendar calendar = Calendar.getInstance();
                String sql = "replace into HistoryHotSearch(search_key, real_url, create_time, year, month, day) values (" +
                        "'" + mData.get(position).searchKey + "', " +
                        "'" + mData.get(position).realURL + "', " +
                        "'" + mData.get(position).createTime + "', " +
                        calendar.get(Calendar.YEAR) + ", " +
                        (calendar.get(Calendar.MONTH) + 1) + ", " +
                        (calendar.get(Calendar.DAY_OF_MONTH))+ ");";
                db.execSQL(sql);
                Bundle bundle = new Bundle();
                bundle.putString("real_url", mData.get(position).realURL);
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        todayHolder.heatText.setText(String.valueOf(mData.get(position).heat));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
