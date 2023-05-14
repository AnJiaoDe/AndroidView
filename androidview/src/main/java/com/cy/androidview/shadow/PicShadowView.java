package com.cy.androidview.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.R;

public class PicShadowView extends View {
    private Paint paint;
    private Bitmap bitmap, bitmapAlpha;
    private int width, height;
    private int left_pic, top_pic, right_pic, bottom_pic;
    private Rect rect;
    private int drawableSrc;
    private int color_shadow;
//    private int scaleType = 0;
    private int shadow_limit = 5;

    public PicShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PicShadowView);
        drawableSrc = typedArray.getResourceId(R.styleable.PicShadowView_cy_src, -1);
        color_shadow = typedArray.getColor(R.styleable.PicShadowView_cy_color_shadow, 0xff656565);
//        scaleType = typedArray.getInt(R.styleable.PicShadowView_cy_scaleType, scaleType);
        this.shadow_limit = typedArray.getDimensionPixelSize(R.styleable.PicShadowView_cy_shadow_limit, shadow_limit);

        typedArray.recycle();

        if (drawableSrc != -1) {
            bitmap = BitmapUtils.decodeBitmapFromResource(context, drawableSrc, 2000 * 2000);
            bitmapAlpha = bitmap.extractAlpha();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color_shadow);
        paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        if(bitmap==null)return;
        float ratio_w = width * 1.0f / bitmap.getWidth();
        float ratio_h = height * 1.0f / bitmap.getHeight();
        int width_b ;
        int height_b;
        if(ratio_w>ratio_h){
            height_b= (int) (height/ratio_h);
            width_b= (int) (height_b*1f*bitmap.getWidth()/bitmap.getHeight());
        }else {
            width_b= (int) (width/ratio_w);
            height_b= (int) (width_b*1f*bitmap.getHeight()/bitmap.getWidth());
        }

        left_pic = (int) ((width - width_b) * 0.5f);
        top_pic = (int) ((height - height_b) * 0.5f);
        right_pic = left_pic + width_b;
        bottom_pic = top_pic + height_b;
        rect = new Rect(left_pic, top_pic, right_pic, bottom_pic);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap==null||rect==null)return;
        canvas.drawBitmap(bitmapAlpha, null, rect, paint);
        canvas.translate(-shadow_limit, -shadow_limit);
        canvas.drawBitmap(bitmap, null, rect, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bitmap != null) bitmap.recycle();
        bitmap = null;
        if (bitmapAlpha != null) bitmapAlpha.recycle();
        bitmapAlpha = null;
    }
}
