package com.cy.androidview.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
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

    private StickerAttr stickerAttr;

    public StickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        listSticker = new ArrayList<>();
        stickerAttr = new StickerAttr(context, attrs);
    }

    public StickerAttr getStickerAttr() {
        return stickerAttr;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void addSticker(Sticker sticker) {
        listSticker.add(sticker);
        invalidate();
    }

    public List<Sticker> getListSticker() {
        return listSticker;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < listSticker.size(); i++) {
            Sticker sticker = listSticker.get(i);
            sticker.onDraw(canvas, stickerAttr);
        }
    }

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
                    if (sticker.getRectFRotateRotated().contains(downX, downY)) {
                        index_rotateZ = i;
                        break;
                    }
                    if (sticker.getRectF3DRotated().contains(downX, downY)) {
                        index_rotate3D = i;
                        break;
                    }
                    if (sticker.getRectFCloseRotated().contains(downX, downY)) {
                        index_down = i;
                        downIn = DOWN_CLOSE;
                        break;
                    }
                    if (sticker.getRectFCopyRotated().contains(downX, downY)) {
                        index_down = i;
                        downIn = DOWN_COPY;
                        break;
                    }
                    float[] points_touch_origin = new float[2];
                    sticker.getMatrix_invert().mapPoints(points_touch_origin, new float[]{downX, downY});
                    if (sticker.getRectF_box_normal().contains(points_touch_origin[0], points_touch_origin[1])) {
                        index_down = i;
                        downIn = DOWN_BOX;
                        break;
                    }
                }
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (downIn == DOWN_BOX) {
                    index_2_pointer = index_down;
                    distance_last = getFingerDistance(event);
                    return true;
                } else {
                    //注意：应该倒叙遍历，因为后添加的在上层，
                    for (int i = listSticker.size() - 1; i >= 0; i--) {
                        Sticker sticker = listSticker.get(i);
                        float[] points_touch_origin = new float[2];
                        //注意是getX(1)
                        sticker.getMatrix_invert().mapPoints(points_touch_origin, new float[]{event.getX(1), event.getY(1)});
                        if (sticker.getRectF_box_normal().contains(points_touch_origin[0], points_touch_origin[1])) {
                            index_2_pointer = i;
                            distance_last = getFingerDistance(event);
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (index_2_pointer >= 0 && index_2_pointer < listSticker.size() && event.getPointerCount() >= 2) {
                    float distance = getFingerDistance(event);
                    Sticker sticker = listSticker.get(index_2_pointer);
                    sticker.setScale(Math.max(0.1f, Math.min(100, sticker.getScale() + 0.005f * (distance - distance_last))));
                    distance_last = distance;
                    invalidate();
                    break;
                }
                if (index_rotateZ >= 0 && index_rotateZ < listSticker.size()) {
                    Sticker sticker = listSticker.get(index_rotateZ);

                    float dx = event.getX() - sticker.getCenterX();
                    float dy = event.getY() - sticker.getCenterY();
                    double angle = Math.toDegrees(Math.atan2(dy, dx));
                    sticker.setRotationZ((float) angle);

                    sticker.setScale((float) (Math.sqrt(Math.pow(event.getX() - sticker.getCenterX(), 2)
                            + Math.pow(event.getY() - sticker.getCenterY(), 2))
                            / Math.sqrt(Math.pow(sticker.getRectF_box_normal().width() * 0.5f, 2)
                            + Math.pow(sticker.getRectF_box_normal().height() * 0.5f, 2))));

                    invalidate();
                    break;
                }
                if (index_rotate3D >= 0 && index_rotate3D < listSticker.size()) {
                    Sticker sticker = listSticker.get(index_rotate3D);

                    float dx = event.getX() - moveX_last;
                    float dy = event.getY() - moveY_last;
                    moveX_last = event.getX();
                    moveY_last = event.getY();

                    sticker.setRotationX(sticker.getRotationX() - dy);
                    sticker.setRotationY(sticker.getRotationY() + dx);

                    invalidate();
                    break;
                }
                if (index_down >= 0 && index_down < listSticker.size()
                        && System.currentTimeMillis() - downTime > TIME_CLICK_THRESHOLD) {
                    Sticker sticker = listSticker.get(index_down);
                    sticker.setCenterX(Math.min(Math.max(0, event.getX()), getWidth()));
                    sticker.setCenterY(Math.min(Math.max(0, event.getY()), getHeight()));

                    invalidate();
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
     * @param x
     * @param y
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

        public void onBoxClick(int index);

        public void onCloseClick(int index);

        public void onCopyClick(int index);
    }
}
