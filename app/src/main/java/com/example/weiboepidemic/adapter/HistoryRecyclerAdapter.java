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
import androidx.recyclerview.widget.RecyclerView;

import com.example.weiboepidemic.MyApplication;
import com.example.weiboepidemic.R;
import com.example.weiboepidemic.entity.HotSearchEntity;
import com.example.weiboepidemic.ui.WebViewActivity;

import java.util.Calendar;
import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<HotSearchEntity> mData;

    public HistoryRecyclerAdapter(Context context, List<HotSearchEntity> data) {
        mContext = context;
        mData = data;
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {
        public TextView keywordText;
        public TextView dateText;
        public TextView heatText;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            keywordText = itemView.findViewById(R.id.keyword);
            dateText = itemView.findViewById(R.id.date);
            heatText = itemView.findViewById(R.id.heat);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_history_recycler, parent, false);
        return new HistoryRecyclerAdapter.HistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryRecyclerAdapter.HistoryHolder historyHolder = (HistoryRecyclerAdapter.HistoryHolder) holder;
        historyHolder.keywordText.setText("#" + mData.get(position).searchKey + "#");
        historyHolder.dateText.setText(mData.get(position).createTime);
        historyHolder.heatText.setText(String.valueOf(mData.get(position).heat));
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
                        calendar.get(Calendar.DAY_OF_MONTH) + ");";
                db.execSQL(sql);
//            db.execSQL("replace into " +
//                        "HistoryHotSearch(search_key, real_url, create_time, year, month, day) " +
//                        "values ('mData.get(position).searchKey')");
//                ContentValues values = new ContentValues();
//                values.put("search_key", mData.get(position).searchKey);
//                values.put("real_url", mData.get(position).realURL);
//                values.put("create_time", mData.get(position).createTime);
//
//                values.put("year", calendar.get(Calendar.YEAR));
//                values.put("month", calendar.get(Calendar.MONTH)+1);
//                values.put("day", calendar.get(Calendar.DAY_OF_MONTH));
//                db.insert("HistoryHotSearch", null, values);
                Bundle bundle = new Bundle();
                bundle.putString("real_url", mData.get(position).realURL);
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
