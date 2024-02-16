package com.cy.necessaryviewmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Size;
import android.view.View;

import com.cy.androidview.LogUtils;
import com.cy.androidview.sticker.Sticker;
import com.cy.androidview.sticker.StickerView;

public class StickActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        StickerView stickerView = findViewById(R.id.StickerView);
        Sticker sticker = new Sticker(this, Sticker.TYPE_TEXT, "贴纸文字");
        sticker.setCenterX(540)
                .setCenterY(500)
                .setRotationX(30);
        stickerView.addSticker(sticker);

        Sticker sticker2 = new Sticker(this, Sticker.TYPE_TEXT, "贴纸文字2");
        sticker2.setCenterX(540)
                .setCenterY(1000)
//                .setRotationX(30)
//                .setRotationY(30)
//                .setRotationZ(30)
//                .setScale(1.5f)
        ;
        stickerView.addSticker(sticker2);

        stickerView.setCallback(new StickerView.Callback() {
            @Override
            public void onBoxClick(int index) {
                LogUtils.log("onBoxClick",index);
            }

            @Override
            public void onCloseClick(int index) {
                LogUtils.log("onCloseClick",index);
            }

            @Override
            public void onCopyClick(int index) {
                LogUtils.log("onCopyClick",index);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}