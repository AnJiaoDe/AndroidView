package com.cy.androidview.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;

public class ImageViewShadowSelector extends View {

    private Paint paint, paint_filter, paintShadow;
    private Bitmap bitmap, bitmapAlpha;

    private int width, height;
    private int left_pic, top_pic, right_pic, bottom_pic;
    private Rect rect;

    // selector
    private int drawableSrcChecked;
    private int drawableSrcUnChecked;
    private int currentResId = -1;

    private int colorFilter;
    private int color_shadow;
    private int shadow_limit = 0;

    private int mask_type = 1;
    private int blur_radius = 1;

    private boolean isChecked = false;
    private boolean isMyListener = true;
    private OnCheckedChangeListener onCheckedChangeListener;

    public ImageViewShadowSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        TypedArray typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.ImageViewShadowSelector
        );

        drawableSrcUnChecked = typedArray.getResourceId(
                R.styleable.ImageViewShadowSelector_cy_srcUnChecked,
                -1
        );

        drawableSrcChecked = typedArray.getResourceId(
                R.styleable.ImageViewShadowSelector_cy_srcChecked,
                -1
        );

        isChecked = typedArray.getBoolean(
                R.styleable.ImageViewShadowSelector_cy_checked,
                false
        );

        colorFilter = typedArray.getColor(
                R.styleable.ImageViewShadowSelector_cy_color_filter,
                -1
        );

        color_shadow = typedArray.getColor(
                R.styleable.ImageViewShadowSelector_cy_color_shadow,
                0xff616161
        );

        shadow_limit = ScreenUtils.dpAdapt(context, 0.4f);
        shadow_limit = typedArray.getDimensionPixelSize(
                R.styleable.ImageViewShadowSelector_cy_shadow_limit,
                shadow_limit
        );

        blur_radius = Math.max(
                1,
                typedArray.getDimensionPixelSize(
                        R.styleable.ImageViewShadowSelector_cy_blur_radius,
                        blur_radius
                )
        );

        mask_type = typedArray.getInt(
                R.styleable.ImageViewShadowSelector_cy_mask_type,
                mask_type
        );

        typedArray.recycle();

        // init image
        if (isChecked) {
            setResOnChecked();
        } else {
            setResOnUnChecked();
        }

        // paint
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint_filter = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (colorFilter != -1) {
            paint_filter.setColor(colorFilter);
        }

        paintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintShadow.setColor(color_shadow);

        switch (mask_type) {
            case 0:
                paintShadow.setMaskFilter(
                        new BlurMaskFilter(
                                blur_radius,
                                BlurMaskFilter.Blur.NORMAL
                        )
                );
                break;

            case 1:
                paintShadow.setMaskFilter(
                        new BlurMaskFilter(
                                blur_radius,
                                BlurMaskFilter.Blur.SOLID
                        )
                );
                break;

            case 2:
                paintShadow.setMaskFilter(
                        new BlurMaskFilter(
                                blur_radius,
                                BlurMaskFilter.Blur.OUTER
                        )
                );
                break;

            case 3:
                paintShadow.setMaskFilter(
                        new BlurMaskFilter(
                                blur_radius,
                                BlurMaskFilter.Blur.INNER
                        )
                );
                break;
        }

        // click selector
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!isChecked);
            }
        });
    }

    @Override
    protected void onLayout(
            boolean changed,
            int left,
            int top,
            int right,
            int bottom
    ) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();

        if (bitmap == null) return;

        int width__ =
                width - getPaddingLeft() - getPaddingRight();

        int height__ =
                height - getPaddingTop() - getPaddingBottom();

        float ratio_s = height__ * 1f / width__;
        float ratio_b = bitmap.getHeight() * 1f / bitmap.getWidth();

        int width_b;
        int height_b;

        if (ratio_s < ratio_b) {
            height_b = height__;
            width_b = (int) (height_b / ratio_b);
        } else {
            width_b = width__;
            height_b = (int) (width_b * ratio_b);
        }

        left_pic =
                getPaddingLeft()
                        + (width__ - width_b) / 2;

        top_pic =
                getPaddingTop()
                        + (height__ - height_b) / 2;

        right_pic = left_pic + width_b;
        bottom_pic = top_pic + height_b;

        rect = new Rect(
                left_pic,
                top_pic,
                right_pic,
                bottom_pic
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap == null || rect == null) return;

        if (bitmapAlpha != null) {
            canvas.drawBitmap(
                    bitmapAlpha,
                    null,
                    rect,
                    paintShadow
            );
        }

        canvas.translate(
                -shadow_limit,
                -shadow_limit
        );

        if (colorFilter != -1 && bitmapAlpha != null) {
            canvas.drawBitmap(
                    bitmapAlpha,
                    null,
                    rect,
                    paint_filter
            );
        } else {
            canvas.drawBitmap(
                    bitmap,
                    null,
                    rect,
                    paint
            );
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = null;

        if (bitmapAlpha != null && !bitmapAlpha.isRecycled()) {
            bitmapAlpha.recycle();
        }
        bitmapAlpha = null;
    }

    //================ selector ================

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (isMyListener) {
            super.setOnClickListener(l);
            isMyListener = false;
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(
                ImageViewShadowSelector view,
                boolean isChecked
        );
    }

    public void setOnCheckedChangeListener(
            OnCheckedChangeListener listener
    ) {
        this.onCheckedChangeListener = listener;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {

        if (isChecked == checked) return;

        isChecked = checked;

        if (checked) {
            setResOnChecked();
        } else {
            setResOnUnChecked();
        }

        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(
                    this,
                    isChecked
            );
        }
    }

    private void setResOnChecked() {
        if (drawableSrcChecked != -1) {
            setImageResource(drawableSrcChecked);
        }
    }

    private void setResOnUnChecked() {
        if (drawableSrcUnChecked != -1) {
            setImageResource(drawableSrcUnChecked);
        }
    }

    //================ image ================

    public void setImageResource(@DrawableRes int resID) {

        if (currentResId == resID) return;
        currentResId = resID;

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        bitmap = BitmapUtils.decodeResource(
                getContext(),
                resID,
                2000 * 2000
        );

        if (bitmapAlpha != null
                && !bitmapAlpha.isRecycled()) {
            bitmapAlpha.recycle();
        }

        bitmapAlpha = null;

        if (bitmap != null) {
            bitmapAlpha = bitmap.extractAlpha();
        }

        requestLayout();
        invalidate();
    }

    public void setImageBitmap(Bitmap bitmap) {

        if (this.bitmap != null
                && !this.bitmap.isRecycled()) {
            this.bitmap.recycle();
        }

        this.bitmap = bitmap;

        if (bitmapAlpha != null
                && !bitmapAlpha.isRecycled()) {
            bitmapAlpha.recycle();
        }

        bitmapAlpha = null;

        if (bitmap != null) {
            bitmapAlpha = bitmap.extractAlpha();
        }

        currentResId = -1;

        requestLayout();
        invalidate();
    }

    public void setColorFilter(int colorFilter) {
        this.colorFilter = colorFilter;

        if (colorFilter != -1) {
            paint_filter.setColor(colorFilter);
        }

        invalidate();
    }

    public void clearColorFilter() {
        colorFilter = -1;
        invalidate();
    }
}