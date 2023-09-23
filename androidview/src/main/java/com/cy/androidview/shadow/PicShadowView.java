package com.cy.androidview.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;
import com.cy.androidview.selectorview.ImageViewSelector;

public class PicShadowView extends View {
    private Paint paint, paint_checked, paintShadow;
    private Bitmap bitmap, bitmapAlpha;
    private int width, height;
    private int left_pic, top_pic, right_pic, bottom_pic;
    private Rect rect;
    private int drawableSrc;
    private int drawableSrcChecked;
    private int colorSrcChecked;
    private int color_shadow;
    //    private int scaleType = 0;
    private int shadow_limit = 0;

    private OnCheckedChangeListener onCheckedChangeListener;
    private boolean isChecked = false;
    private boolean isMyListener = true;
    private int mask_type = 1;
    private int blur_radius = 1;

    public PicShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PicShadowView);
        drawableSrc = typedArray.getResourceId(R.styleable.PicShadowView_cy_src, -1);
        drawableSrcChecked = typedArray.getResourceId(R.styleable.PicShadowView_cy_src_checked, -1);
        colorSrcChecked = typedArray.getColor(R.styleable.PicShadowView_cy_src_color_checked, -1);//是否选中
        color_shadow = typedArray.getColor(R.styleable.PicShadowView_cy_color_shadow, 0xff616161);
        shadow_limit = typedArray.getDimensionPixelSize(R.styleable.PicShadowView_cy_shadow_limit, shadow_limit);
        //BlurMaskFilter的blur_radius  must >0，否则GG
        blur_radius = Math.max(blur_radius, typedArray.getDimensionPixelSize(R.styleable.PicShadowView_cy_blur_radius, blur_radius));
        isChecked = typedArray.getBoolean(R.styleable.PicShadowView_cy_checked, false);//是否选中
        mask_type = typedArray.getInt(R.styleable.PicShadowView_cy_mask_type, mask_type);

        typedArray.recycle();

        if (isChecked()) {
            setResOnChecked();
        } else {
            setResOnUnChecked();
        }
        if (bitmap != null)
            bitmapAlpha = bitmap.extractAlpha();

        paint = new Paint();
        paint.setAntiAlias(true);

        paint_checked = new Paint();
        paint_checked.setAntiAlias(true);
        if (colorSrcChecked != -1) paint_checked.setColor(colorSrcChecked);

        paintShadow = new Paint();
        paintShadow.setAntiAlias(true);
        paintShadow.setColor(color_shadow);
        switch (mask_type) {
            case 0:
                paintShadow.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.NORMAL));
                break;
            case 1:
                paintShadow.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.SOLID));
                break;
            case 2:
                paintShadow.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.OUTER));
                break;
            case 3:
                paintShadow.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.INNER));
                break;
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!isChecked);
                if (onCheckedChangeListener != null)
                    onCheckedChangeListener.onCheckedChanged(PicShadowView.this, isChecked);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        if (bitmap == null) return;

        int width__ = width - getPaddingLeft() - getPaddingRight();
        int height__ = height - getPaddingTop() - getPaddingBottom();
        float ratio_s = height__ * 1f / width__;
        float ratio_b = bitmap.getHeight() * 1f / bitmap.getWidth();

        int width_b;
        int height_b;
        if (ratio_s < ratio_b) {
            height_b = height__;
            width_b = (int) (height_b * 1f / ratio_b);
        } else {
            width_b = width__;
            height_b = (int) (width_b * 1f * ratio_b);
        }

        left_pic = (int) (getPaddingLeft() + (width__ - width_b) * 0.5f);
        top_pic = (int) (getPaddingTop() + (height__ - height_b) * 0.5f);
        right_pic = left_pic + width_b;
        bottom_pic = top_pic + height_b;
        rect = new Rect(left_pic, top_pic, right_pic, bottom_pic);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null || rect == null) return;
        canvas.drawBitmap(bitmapAlpha, null, rect, paintShadow);
        canvas.translate(-shadow_limit, -shadow_limit);
        if (isChecked && drawableSrcChecked == -1 && colorSrcChecked != -1) {
            canvas.drawBitmap(bitmapAlpha, null, rect, paint_checked);
        } else {
            canvas.drawBitmap(bitmap, null, rect, paint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bitmap != null) bitmap.recycle();
        bitmap = null;
        if (bitmapAlpha != null) bitmapAlpha.recycle();
        bitmapAlpha = null;
    }

    //判断是否选中
    public boolean isChecked() {
        return isChecked;
    }

    //设置是否选中
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (checked) {
            setResOnChecked();
        } else {
            setResOnUnChecked();
        }
        if (bitmap != null)
            bitmapAlpha = bitmap.extractAlpha();
        //因为需要计算图片宽高，所以必须先requestLayout
        requestLayout();
        invalidate();
    }
    //设置选中时的背景，Src等

    private void setResOnChecked() {
        if (drawableSrcChecked != -1)
            bitmap = BitmapUtils.decodeBitmapFromResource(getContext(), drawableSrcChecked, 2000 * 2000);
    }

    //设置未选中时的背景，Src等
    private void setResOnUnChecked() {
        if (drawableSrc != -1)
            bitmap = BitmapUtils.decodeBitmapFromResource(getContext(), drawableSrc, 2000 * 2000);
    }

    public void setImageResource(@DrawableRes int resID) {
        drawableSrc=resID;
        setChecked(isChecked);
    }
    public void setImageResourceChecked(@DrawableRes int resID) {
        drawableSrcChecked=resID;
        setChecked(isChecked);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (isMyListener) {
            super.setOnClickListener(l);
            isMyListener = false;
        }
    }

    //监听器使用这个方法
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(PicShadowView picShadowView, boolean isChecked);
    }
}
