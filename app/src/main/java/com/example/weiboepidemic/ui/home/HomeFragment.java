package com.example.weiboepidemic.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weiboepidemic.R;
import com.example.weiboepidemic.ui.DownloadFormActivity;
import com.example.weiboepidemic.ui.HistoryListActivity;
import com.example.weiboepidemic.ui.QueryHistoryActivity;

public class HomeFragment extends Fragment {
    private Button queryBtn;
    private Button tableBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        return root;
    }

    public void initView(View root) {
        queryBtn = root.findViewById(R.id.query_history);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QueryHistoryActivity.class);
                startActivity(intent);
            }
        });
        tableBtn = root.findViewById(R.id.hot_search_table);
        tableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DownloadFormActivity.class);
                startActivity(intent);
            }
        });
    }
}