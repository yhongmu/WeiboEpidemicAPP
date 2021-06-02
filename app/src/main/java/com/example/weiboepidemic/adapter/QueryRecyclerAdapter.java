package com.example.weiboepidemic.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weiboepidemic.R;
import com.example.weiboepidemic.entity.HotSearchEntity;
import com.example.weiboepidemic.entity.LocalHotSearchEntity;
import com.example.weiboepidemic.ui.WebViewActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class QueryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<LocalHotSearchEntity> mData;

    public QueryRecyclerAdapter(Context context, List<LocalHotSearchEntity> data) {
        mContext = context;
        mData = data;
    }

    public static class QueryHolder extends RecyclerView.ViewHolder {
        public TextView keywordText;
        public TextView dateText;
        public TextView date2Text;

        public QueryHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.create_date);
            keywordText = itemView.findViewById(R.id.search_key);
            date2Text = itemView.findViewById(R.id.histoty_date);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_query_recycler, parent, false);
        return new QueryRecyclerAdapter.QueryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QueryRecyclerAdapter.QueryHolder queryHolder = (QueryRecyclerAdapter.QueryHolder) holder;
        queryHolder.keywordText.setText(mData.get(position).searchKey);
        queryHolder.dateText.setText(mData.get(position).createTime);
        Calendar calendar = Calendar.getInstance();
        String historyDateStr;
        if (calendar.get(Calendar.MONTH) + 1 - mData.get(position).month > 1) {
            historyDateStr = "上个月以前";
        } else if (calendar.get(Calendar.MONTH) + 1 - mData.get(position).month == 1){
            historyDateStr = "上个月";
        } else if (calendar.get(Calendar.DAY_OF_MONTH) == mData.get(position).day) {
            historyDateStr = "今天";
        } else if (calendar.get(Calendar.DAY_OF_MONTH) - mData.get(position).day == 1){
            historyDateStr = "昨天";
        } else if (calendar.get(Calendar.DAY_OF_MONTH) - mData.get(position).day == 2){
            historyDateStr = "前天";
        } else {
            historyDateStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) - mData.get(position).day) + "天以前";
        }
        queryHolder.date2Text.setText(historyDateStr);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
