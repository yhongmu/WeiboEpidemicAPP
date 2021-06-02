package com.example.weiboepidemic.ui.history;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.weiboepidemic.R;
import com.example.weiboepidemic.ui.HistoryListActivity;

import java.text.DecimalFormat;

public class HistoryFragment extends Fragment implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private TextView startText;
    private TextView endText;
    private EditText heatEdit;
    private Button screenBtn;
    private int startYear = 2020, startMonth = 1, startDay = 1;
    private int endYear = 2020, endMonth = 1, endDay = 1;
    private StringBuffer startDate, endDate;
    private DatePicker startDatePicker, endDatePicker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);
        initView(root);
        return root;
    }

    public void initView(View root) {
        startDate = new StringBuffer();
        endDate = new StringBuffer();
        startText = root.findViewById(R.id.start_date);
        endText = root.findViewById(R.id.end_date);
        heatEdit = root.findViewById(R.id.heat_edit);
        screenBtn = root.findViewById(R.id.screen_btn);
        startText.setOnClickListener(this);
        endText.setOnClickListener(this);
        screenBtn.setOnClickListener(this);

    }

    /**
     * 日期改变的监听事件
     *
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(view == startDatePicker) {
            startYear = year;
            startMonth = monthOfYear + 1;
            startDay = dayOfMonth;
        } else {
            endYear = year;
            endMonth = monthOfYear + 1;
            endDay = dayOfMonth;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.screen_btn:
                    String heatStr = heatEdit.getText().toString();
                    if (!heatStr.equals("0") && !heatStr.equals("")) {
                        heatStr += "0000";
                    }
                    String startDateStr = startText.getText().toString();
                    String endDateStr = endText.getText().toString();
                    if (startDateStr.equals("") || endDateStr.equals("")) {
                        Toast.makeText(getContext(),
                                "请选择起始或结束日期！", Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("heat", heatStr);
                        bundle.putString("start_date", startDateStr);
                        bundle.putString("end_date", endDateStr);
                        Intent intent = new Intent(getActivity(), HistoryListActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                break;
            case R.id.start_date:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (startDate.length() > 0) { //清除上次记录的日期
                            startDate.delete(0, startDate.length());
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("00");
                        String monthStr = decimalFormat.format(startMonth);
                        String dayStr = decimalFormat.format(startDay);
                        startDate.append(startYear).append("-").append(monthStr).append("-").append(dayStr);
                        startText.setText(startDate);
                        if (endText.getText() == "") {
                            endText.setText(startDate);
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(getContext(), R.layout.dialog_date, null);
                startDatePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);

                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                startDatePicker.init(startYear, startMonth-1, startDay, this);
                break;
            case R.id.end_date:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                builder2.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (endDate.length() > 0) { //清除上次记录的日期
                            endDate.delete(0, endDate.length());
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("00");
                        String monthStr = decimalFormat.format(endMonth);
                        String dayStr = decimalFormat.format(endDay);
                        endDate.append(endYear).append("-").append(monthStr).append("-").append(dayStr);
                        endText.setText(endDate);
                        if (startText.getText() == "") {
                            startText.setText(endDate);
                        }
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(getContext(), R.layout.dialog_end_date, null);
                endDatePicker = (DatePicker) dialogView2.findViewById(R.id.endDatePicker);

                dialog2.setTitle("设置日期");
                dialog2.setView(dialogView2);
                dialog2.show();
                //初始化日期监听事件
                endDatePicker.init(endYear, endMonth-1, endDay, this);
                break;
        }
    }
}