package com.cy.necessaryviewmaster;

import static androidx.core.util.Preconditions.checkNotNull;

import android.gesture.Prediction;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.androidview.progress.ProgressWeightView;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_click).setOnClickListener(this);
        findViewById(R.id.btn_rectangle).setOnClickListener(this);
        findViewById(R.id.btn_shape).setOnClickListener(this);
        findViewById(R.id.btn_card).setOnClickListener(this);
        findViewById(R.id.btn_roundiv).setOnClickListener(this);
        findViewById(R.id.btn_selector).setOnClickListener(this);
        findViewById(R.id.btn_shadow).setOnClickListener(this);
        findViewById(R.id.btn_progress).setOnClickListener(this);

        String str = "各个大哥更";
        for (String s : str.split("")) {
            LogUtils.log("String", s);
        }

        /**
         * flag 参数的作用
         * flag 参数是一个整数值，指定如何处理新添加的文本的样式和格式。常见的标志值包括：
         * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE: 新文本的样式不包括在原文本中（即，样式只应用于新文本）。
         * Spannable.SPAN_EXCLUSIVE_INCLUSIVE: 新文本的样式从其开始位置开始，但不包括原文本的结束位置。
         * Spannable.SPAN_INCLUSIVE_EXCLUSIVE: 新文本的样式从原文本的起始位置开始，但不包括新文本的结束位置。
         * Spannable.SPAN_INCLUSIVE_INCLUSIVE: 新文本的样式包括原文本的起始和结束位置。
         */
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("Hello, ");
        builder.append("colorful", new ForegroundColorSpan(Color.RED), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" and ");
        builder.append("size varied", new RelativeSizeSpan(2f), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(" text!");

        TextView tv = findViewById(R.id.tv);
        tv.setText(builder);


        // 添加文本
        builder.append("Hello, ");
        int start = builder.length();
        builder.append("world!");
        int end = builder.length();

        // 设置粗体
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置斜体
        builder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置文本颜色
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置文本相对大小
        builder.setSpan(new RelativeSizeSpan(1.5f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置 SpannableStringBuilder 为 TextView 的内容
        tv.setText(builder);

        ProgressWeightView progressWeightView = findViewById(R.id.ProgressWeightView);
        progressWeightView.setProgress(0.1f);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_click:
                startAppcompatActivity(ClickActivity.class);
                break;
            case R.id.btn_rectangle:
                startAppcompatActivity(RectangleActivity.class);
                break;
            case R.id.btn_shape:
                startAppcompatActivity(ShapeActivity.class);
                break;
            case R.id.btn_card:
                startAppcompatActivity(CardActivity.class);
                break;
            case R.id.btn_roundiv:
                startAppcompatActivity(RoundedIVActivity.class);
                break;
            case R.id.btn_selector:
                startAppcompatActivity(SelectorActivity.class);
                break;
            case R.id.btn_shadow:
                startAppcompatActivity(ShadowActivity.class);
                break;
            case R.id.btn_progress:
                startAppcompatActivity(ProgressActivity.class);
                break;
        }
    }
}
