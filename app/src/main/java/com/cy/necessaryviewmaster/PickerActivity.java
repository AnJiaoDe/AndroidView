package com.cy.necessaryviewmaster;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cy.androidview.pickerview.DateWheelPickerView;

import java.util.Calendar;

public class PickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        DateWheelPickerView picker = findViewById(R.id.datePicker);

        picker.setDateRange(
                getMillis(1025, 10, 1),
                getMillis(2026, 10, 31)
        );
        picker.setDateRange(
                2025, 10, 1,
                2026, 10, 31
        );
//        picker.setSelectedDate(getMillis(2026, 10, 21));

// 设置分割线颜色
//        picker.setDividerColor(Color.parseColor("#E0E0E0"));
//
//// 设置分割线粗细，单位 dp
//        picker.setDividerHeight(1);

// 设置选中和未选中文字颜色
//        picker.setSelectedTextColor(Color.BLACK);
//        picker.setNormalTextColor(Color.GRAY);

// 日期变化回调
        picker.setCallback((year, month, day, timeMillis) -> {
            // 获取当前选中的日期
            LogUtils.log("setCallback",year);
            LogUtils.log("setCallback  ",month);
            LogUtils.log("setCallback   ",day);
        });
    }

    private long getMillis(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month - 1, day);
        return c.getTimeInMillis();
    }
}