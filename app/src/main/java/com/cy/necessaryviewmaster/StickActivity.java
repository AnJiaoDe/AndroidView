package com.cy.necessaryviewmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.LogUtils;
import com.cy.androidview.sticker.Sticker;
import com.cy.androidview.sticker.StickerView;
import com.cy.androidview.sticker.WatermarkStickerView;

import java.util.HashMap;
import java.util.Map;

public class StickActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        WatermarkStickerView watermarkStickerView = findViewById(R.id.WatermarkStickerView);
        Sticker sticker = new Sticker(this, Sticker.TYPE_TEXT, "贴纸\n文字");
        sticker.setCenterX(540)
                .setCenterY(100)
                .setRotationX(30)
                .setRotationY(30)
                .setRotationZ(30)
                .setScale(1.2f)

        ;
        watermarkStickerView.addSticker(sticker);


        Sticker sticker2 = new Sticker(this, Sticker.TYPE_TEXT, "#*￥$(35额的额服务\n哥哥风歌他");
        sticker2.setTextAlign(Paint.Align.RIGHT);
        sticker2.setLetterSpacing(1f);
        sticker2.setCenterX(540)
                .setCenterY(1000)
                .setVertical(true)
                .setLineSpace(2)
        ;
        watermarkStickerView.addSticker(sticker2);

        Sticker sticker3 = new Sticker(this, Sticker.TYPE_TEXT, "代加工第几个i诶过\nefefeg淀粉");
        sticker3.setTextAlign(Paint.Align.CENTER);
        sticker3.setLetterSpacing(1f);
        sticker3.setTypeface("", Typeface.BOLD | Typeface.ITALIC);

//        sticker3.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //会出现2条横线，原因不明
//        sticker3.setFlags(sticker3.getFlags() | Paint.UNDERLINE_TEXT_FLAG);
        float blur_radius = 50;
        sticker3.setMaskFilter(blur_radius);
        sticker3.setShadowLayer(10, 10, 10, Color.RED);
        sticker3.setCenterX(540)
                .setCenterY(1600)
                .setLineSpace(0.5f);
        ;
        watermarkStickerView.addSticker(sticker3);
        watermarkStickerView.setText_text("日常自拍大哥哥特特瑞特任特特大肥肥我");
        watermarkStickerView.setShadowLayer(10,10,10,Color.RED);
//        watermarkStickerView.setMargin_y()
        watermarkStickerView.setShowTime(true);

        watermarkStickerView.setCallback(new StickerView.Callback() {
            @Override
            public void onOutsideClick() {
                LogUtils.log("onOutsideClick");
            }

            @Override
            public void onXYChanged(int index) {

            }

            @Override
            public void onScaleChanged(int index) {

            }

            @Override
            public void onBoxClick(int index) {
                LogUtils.log("onBoxClick", index);
            }

            @Override
            public void onCloseClick(int index) {
                LogUtils.log("onCloseClick", index);
            }

            @Override
            public void onCopyClick(int index) {
                LogUtils.log("onCopyClick", index);
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker3.setTextAlign(Paint.Align.RIGHT).setVertical(true);
                watermarkStickerView.invalidate();
            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}