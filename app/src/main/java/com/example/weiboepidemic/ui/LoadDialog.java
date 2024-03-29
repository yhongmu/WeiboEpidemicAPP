package com.example.weiboepidemic.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weiboepidemic.R;

public class LoadDialog extends Dialog {

    TextView tvLoadingTx;
    ImageView ivLoading;

    public LoadDialog(Context context) {
        this(context, R.style.load_dialog, "玩命加载中...");

    }

    public LoadDialog(Context context, String string) {
        this(context, R.style.load_dialog, string);
    }

    protected LoadDialog(Context context, int theme, String string) {
        super(context, theme);
        setCanceledOnTouchOutside(false);//点击其他区域时 true 关闭弹窗 false 不关闭弹窗
        setContentView(R.layout.dialog_layout);//加载布局
        tvLoadingTx = findViewById(R.id.tvText);
        tvLoadingTx.setText(string);
        ivLoading = findViewById(R.id.ivProgress);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        ivLoading.startAnimation(hyperspaceJumpAnimation);

        getWindow().getAttributes().gravity = Gravity.CENTER;//居中显示
        getWindow().getAttributes().dimAmount = 0.8f;//背景透明度 取值范围 0 ~ 1
    }

    //关闭弹窗
    @Override
    public void dismiss() {
        super.dismiss();
    }
}