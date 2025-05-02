package com.cy.androidview.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.cy.androidview.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StickerView extends View {
    private List<Sticker> listSticker;
    private int index_rotateZ = -1;
    private int index_rotate3D = -1;
    private int index_down = -1;
    private int index_2_pointer = -1;
    private float downX, downY, moveX_last, moveY_last;
    private long downTime;
    private final int DOWN_CLOSE = 0;
    private final int DOWN_COPY = 1;
    private final int DOWN_BOX = 2;
    private int downIn = -1;
    private Callback callback;
    private final int TIME_CLICK_THRESHOLD = 200;
    private float distance_last;
    private boolean open = true;
    private StickerAttr stickerAttr;

    private Matrix matrixParent;
    private Matrix matrixParentInvert;
    private float[] points_touch_origin;
    private RectF rectF;

    public StickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        listSticker = new ArrayList<>();
        stickerAttr = new StickerAttr(context, attrs);
        matrixParent = new Matrix();
        matrixParentInvert = new Matrix();
        points_touch_origin = new float[2];
        rectF = new RectF();
    }

    public StickerAttr getStickerAttr() {
        return stickerAttr;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        //防止StickerView宽高改变后，文字看不见，贼鸡儿尴尬
//        for (int i = 0; i < listSticker.size(); i++) {
//            Sticker sticker = listSticker.get(i);
//            float w = sticker.getTextWidth()/getWidth() * sticker.getScale() * 0.5f;
//            float h = sticker.getTextHeight()/getHeight() * sticker.getScale() * 0.5f;
//            sticker.setCenterX(Math.max(w, Math.min(1 - w, sticker.getCenterX())))
//                    .setCenterY(Math.max(h, Math.min(1 - h, sticker.getCenterY())));
//            listSticker.set(i, sticker);
//        }
    }

    public void addSticker(Sticker sticker) {
        listSticker.add(sticker);
        invalidate();
    }

    public void setSticker(int index, Sticker sticker) {
        listSticker.set(index, sticker);
        invalidate();
    }

    public void removeSticker(int index) {
        if (index < 0 || index >= listSticker.size()) return;
        listSticker.remove(index);
        invalidate();
    }

    public void clearSticker() {
        listSticker.clear();
        invalidate();
    }

    public void swap(int i, int j) {
        if (i < 0 || i >= listSticker.size() || j < 0 || j >= listSticker.size()) return;
        Collections.swap(listSticker, i, j);
        invalidate();
    }

    public List<Sticker> getListSticker() {
        return listSticker;
    }

    public void setListSticker(List<Sticker> listSticker) {
        this.listSticker = listSticker;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public StickerView onCanvasChange(float scale, float dx, float dy) {
        matrixParent.reset();
        //注意不是setScale setTranslate否则会覆盖之前的
        matrixParent.postScale(scale, scale, getWidth() * 0.5f, getHeight() * 0.5f);
        matrixParent.postTranslate(dx, dy);

        matrixParentInvert.reset();
        matrixParent.invert(matrixParentInvert);
        return this;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!open) return;
        for (int i = 0; i < listSticker.size(); i++) {
            Sticker sticker = listSticker.get(i);
            sticker.onDraw(canvas, getWidth(),getHeight(),stickerAttr);
        }
    }

    /**
     * 1.如果使用FrameLayout包裹StickerView,触摸FrameLayout，对FrameLayout的canvas进行sacle和translate,
     * getRectFCloseRotated和event.getX均是原来的StickerView的位置，没有变化，
     * 这样的话，就必须将FrameLayout的canvas的scale和translate形成的matrix将getRectFCloseRotated映射到缩放和平移后的位置，
     * 2.如果使用FrameLayout包裹StickerView,触摸FrameLayout，然后在Sticker里进行scale 和translate，很复杂
     *
     * @param event The motion event.
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX(0);
                downY = event.getY(0);
                moveX_last = downX;
                moveY_last = downY;
                downTime = System.currentTimeMillis();

                //注意：应该倒叙遍历，因为后添加的在上层，
                for (int i = listSticker.size() - 1; i >= 0; i--) {
                    Sticker sticker = listSticker.get(i);
                    //一定要先判断旋转和点击再判断移动，否则容易导致文本框被移出view之外
                    matrixParent.mapRect(rectF, sticker.getRectFRotateRotated());
                    if (rectF.contains(downX, downY)) {
                        index_rotateZ = i;
                        return true;
                    }
                    matrixParent.mapRect(rectF, sticker.getRectF3DRotated());
                    if (rectF.contains(downX, downY)) {
                        index_rotate3D = i;
                        return true;
                    }
                    matrixParent.mapRect(rectF, sticker.getRectFCloseRotated());
                    if (rectF.contains(downX, downY)) {
                        index_down = i;
                        downIn = DOWN_CLOSE;
                        return true;
                    }
                    matrixParent.mapRect(rectF, sticker.getRectFCopyRotated());
                    if (rectF.contains(downX, downY)) {
                        index_down = i;
                        downIn = DOWN_COPY;
                        return true;
                    }
                    //注意：rect永远是水平和垂直的矩形，不是斜着的，
                    sticker.getMatrix().mapRect(rectF, sticker.getRectF_box_normal());
                    matrixParent.mapRect(rectF, rectF);
                    if (rectF.contains(downX, downY)) {
                        index_down = i;
                        downIn = DOWN_BOX;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //有时候会2个手指分别按在2个sticker上，故而，以第一根手指为准
                if (downIn == DOWN_BOX) {
                    index_2_pointer = index_down;
                    distance_last = getFingerDistance(event);
                } else {
                    //注意：应该倒叙遍历，因为后添加的在上层，
                    for (int i = listSticker.size() - 1; i >= 0; i--) {
                        Sticker sticker = listSticker.get(i);
                        //注意是getX(1)
                        sticker.getMatrix().mapRect(rectF, sticker.getRectF_box_normal());
                        if (rectF.contains(downX, downY)) {
                            index_2_pointer = i;
                            distance_last = getFingerDistance(event);
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                matrixParentInvert.mapPoints(points_touch_origin, new float[]{event.getX(), event.getY()});
                if (index_2_pointer >= 0 && index_2_pointer < listSticker.size() && event.getPointerCount() >= 2) {
                    float distance = getFingerDistance(event);
                    Sticker sticker = listSticker.get(index_2_pointer);
                    sticker.setScale(sticker.getScale() + 0.005f * (distance - distance_last));
                    distance_last = distance;
                    invalidate();
                    if (callback != null) callback.onScaleChanged(index_2_pointer);
                    break;
                }
                if (index_rotateZ >= 0 && index_rotateZ < listSticker.size()) {
                    Sticker sticker = listSticker.get(index_rotateZ);

                    float dx = points_touch_origin[0]/getWidth() - sticker.getCenterX();
                    float dy = points_touch_origin[1]/getHeight() - sticker.getCenterY();
                    double angle = Math.toDegrees(Math.atan2(dy, dx));
                    sticker.setRotationZ((float) angle);

                    sticker.setScale(((float) (Math.sqrt(Math.pow(points_touch_origin[0] /getWidth()- sticker.getCenterX(), 2)
                            + Math.pow(points_touch_origin[1]/getHeight() - sticker.getCenterY(), 2))
                            / Math.sqrt(Math.pow(sticker.getRectF_box_normal().width() * 0.5f, 2)
                            + Math.pow(sticker.getRectF_box_normal().height() * 0.5f, 2)))));
                    invalidate();
                    if (callback != null) callback.onScaleChanged(index_rotateZ);
                    break;

                }
                if (index_rotate3D >= 0 && index_rotate3D < listSticker.size()) {
                    Sticker sticker = listSticker.get(index_rotate3D);

                    float dx = points_touch_origin[0] - moveX_last;
                    float dy = points_touch_origin[1] - moveY_last;
                    moveX_last = points_touch_origin[0];
                    moveY_last = points_touch_origin[1];

                    sticker.setRotationX(sticker.getRotationX() - dy);
                    sticker.setRotationY(sticker.getRotationY() + dx);

                    invalidate();
                    break;

                }
                if (index_down >= 0 && index_down < listSticker.size()
                        && System.currentTimeMillis() - downTime > TIME_CLICK_THRESHOLD) {
                    Sticker sticker = listSticker.get(index_down);
                    sticker.setCenterX(Math.min(Math.max(0, points_touch_origin[0]/getWidth()), 1));
                    sticker.setCenterY(Math.min(Math.max(0, points_touch_origin[1]/getHeight()), 1));
                    invalidate();
                    Sticker.Callback c = sticker.getCallback();
                    if (c != null) c.onXYChanged(sticker.getCenterX(), sticker.getCenterY());
                    if (callback != null) callback.onXYChanged(index_down);
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (System.currentTimeMillis() - downTime < TIME_CLICK_THRESHOLD &&
                        Math.abs(event.getX() - downX) < 10 && Math.abs(event.getY() - downY) < 10) {
                    switch (downIn) {
                        case DOWN_BOX:
                            Sticker sticker = listSticker.get(index_down);
                            sticker.setShowBox(true);
                            invalidate();
                            if (callback != null) callback.onBoxClick(index_down);
                            break;
                        case DOWN_CLOSE:
                            listSticker.remove(index_down);
                            invalidate();
                            if (callback != null) callback.onCloseClick(index_down);
                            break;
                        case DOWN_COPY:
                            listSticker.add(listSticker.get(index_down).copy());
                            invalidate();
                            if (callback != null) callback.onCopyClick(index_down);
                            break;
                        default:
                            showBox(false);
                            if (callback != null) callback.onOutsideClick();
                            break;
                    }
                }
                index_rotateZ = -1;
                index_rotate3D = -1;
                index_down = -1;
                downIn = -1;
                index_2_pointer = -1;
                break;
        }
        return super.onTouchEvent(event);
    }

    public void showBox(boolean showBox) {
        for (Sticker s : listSticker) {
            s.setShowBox(showBox);
        }
        invalidate();
    }

    /**
     * 此函数供外部使用，处理触摸事件抢占
     *
     * @return
     */
    public boolean isXYinAnySticker(float x, float y) {
        boolean in = false;
        //注意：应该倒叙遍历，因为后添加的在上层，
        for (int i = listSticker.size() - 1; i >= 0; i--) {
            Sticker sticker = listSticker.get(i);
            if (sticker.getRectFRotateRotated().contains(x, y)) {
                in = true;
                break;
            }
            if (sticker.getRectF3DRotated().contains(x, y)) {
                in = true;
                break;
            }
            if (sticker.getRectFCloseRotated().contains(x, y)) {
                in = true;
                break;
            }
            if (sticker.getRectFCopyRotated().contains(x, y)) {
                in = true;
                break;
            }
            float[] points_touch_origin = new float[2];
            sticker.getMatrix_invert().mapPoints(points_touch_origin, new float[]{x, y});
            if (sticker.getRectF_box_normal().contains(points_touch_origin[0], points_touch_origin[1])) {
                in = true;
                break;
            }
        }
        return in;
    }
    private float getFingerDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public static interface Callback {
        public void onOutsideClick();

        public void onXYChanged(int index);

        public void onScaleChanged(int index);

        public void onBoxClick(int index);

        public void onCloseClick(int index);

        public void onCopyClick(int index);
    }
}
