package com.cy.androidview.cardview;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class RoundRectDrawable extends Drawable {
    private float mRadius;
    private final Paint mPaint;
    private final RectF mBoundsF;
    private final Rect mBoundsI;
    private float mPadding;
    private boolean mInsetForPadding = false;
    private boolean mInsetForRadius = true;
    private ColorStateList mBackground;
    private PorterDuffColorFilter mTintFilter;
    private ColorStateList mTint;
    private PorterDuff.Mode mTintMode;

    RoundRectDrawable(ColorStateList backgroundColor, float radius) {
        this.mTintMode = PorterDuff.Mode.SRC_IN;
        this.mRadius = radius;
        this.mPaint = new Paint(5);
        this.setBackground(backgroundColor);
        this.mBoundsF = new RectF();
        this.mBoundsI = new Rect();
    }

    private void setBackground(ColorStateList color) {
        this.mBackground = color == null ? ColorStateList.valueOf(0) : color;
        this.mPaint.setColor(this.mBackground.getColorForState(this.getState(), this.mBackground.getDefaultColor()));
    }

    void setPadding(float padding, boolean insetForPadding, boolean insetForRadius) {
        if (padding != this.mPadding || this.mInsetForPadding != insetForPadding || this.mInsetForRadius != insetForRadius) {
            this.mPadding = padding;
            this.mInsetForPadding = insetForPadding;
            this.mInsetForRadius = insetForRadius;
            this.updateBounds((Rect)null);
            this.invalidateSelf();
        }
    }

    float getPadding() {
        return this.mPadding;
    }

    public void draw(Canvas canvas) {
        Paint paint = this.mPaint;
        boolean clearColorFilter;
        if (this.mTintFilter != null && paint.getColorFilter() == null) {
            paint.setColorFilter(this.mTintFilter);
            clearColorFilter = true;
        } else {
            clearColorFilter = false;
        }

        canvas.drawRoundRect(this.mBoundsF, this.mRadius, this.mRadius, paint);
        if (clearColorFilter) {
            paint.setColorFilter((ColorFilter)null);
        }

    }

    private void updateBounds(Rect bounds) {
        if (bounds == null) {
            bounds = this.getBounds();
        }

        this.mBoundsF.set((float)bounds.left, (float)bounds.top, (float)bounds.right, (float)bounds.bottom);
        this.mBoundsI.set(bounds);
        if (this.mInsetForPadding) {
            float vInset = RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
            float hInset = RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
            this.mBoundsI.inset((int)Math.ceil((double)hInset), (int)Math.ceil((double)vInset));
            this.mBoundsF.set(this.mBoundsI);
        }

    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.updateBounds(bounds);
    }

    public void getOutline(Outline outline) {
        outline.setRoundRect(this.mBoundsI, this.mRadius);
    }

    void setRadius(float radius) {
        if (radius != this.mRadius) {
            this.mRadius = radius;
            this.updateBounds((Rect)null);
            this.invalidateSelf();
        }
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return -3;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public void setColor(@Nullable ColorStateList color) {
        this.setBackground(color);
        this.invalidateSelf();
    }

    public ColorStateList getColor() {
        return this.mBackground;
    }

    public void setTintList(ColorStateList tint) {
        this.mTint = tint;
        this.mTintFilter = this.createTintFilter(this.mTint, this.mTintMode);
        this.invalidateSelf();
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mTintMode = tintMode;
        this.mTintFilter = this.createTintFilter(this.mTint, this.mTintMode);
        this.invalidateSelf();
    }

    protected boolean onStateChange(int[] stateSet) {
        int newColor = this.mBackground.getColorForState(stateSet, this.mBackground.getDefaultColor());
        boolean colorChanged = newColor != this.mPaint.getColor();
        if (colorChanged) {
            this.mPaint.setColor(newColor);
        }

        if (this.mTint != null && this.mTintMode != null) {
            this.mTintFilter = this.createTintFilter(this.mTint, this.mTintMode);
            return true;
        } else {
            return colorChanged;
        }
    }

    public boolean isStateful() {
        return this.mTint != null && this.mTint.isStateful() || this.mBackground != null && this.mBackground.isStateful() || super.isStateful();
    }

    private PorterDuffColorFilter createTintFilter(ColorStateList tint, PorterDuff.Mode tintMode) {
        if (tint != null && tintMode != null) {
            int color = tint.getColorForState(this.getState(), 0);
            return new PorterDuffColorFilter(color, tintMode);
        } else {
            return null;
        }
    }
}
