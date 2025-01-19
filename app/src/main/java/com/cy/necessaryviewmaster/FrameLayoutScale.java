//package com.cy.necessaryviewmaster;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.widget.FrameLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//public class FrameLayoutScale extends FrameLayout {
//    private float distance_last;
//    private float zoom = 1; // 缩放
//    private float zoom_max = 10; // 缩放
//    private float zoom_min = 0.1f; // 缩放
//
//    private float downX, downY;
//    private float downX2, downY2;
//    private float translate_x, translate_y;
//
//    private boolean enableZoom = true;
//    private boolean doubleMoveEventEnable = false;
//    private boolean touchEventDownReturnTrue = true;
//    private boolean singleMoveEventEnable = true;
//    private boolean touchEventUpReset = false;
//
//    private Callback callback;
//
//    public FrameLayoutScale(@NonNull Context context) {
//        this(context, null);
//    }
//
//    public FrameLayoutScale(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public void setCallback(Callback callback) {
//        this.callback = callback;
//    }
//
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        canvas.scale(zoom, zoom, getWidth() * 1f / 2 + translate_x, getHeight() * 1f / 2 + translate_y);
//        //必须先scale后translate，否则会往相反方向translate
//        canvas.translate(translate_x, translate_y);
//        if (callback != null) callback.onCanvasChange(zoom, translate_x, translate_y);
//        super.dispatchDraw(canvas);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                LogUtils.log("ACTION_DOWN ");
//                downX = event.getX();
//                downY = event.getY();
//                if (touchEventDownReturnTrue) return true;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                LogUtils.log("ACTION_POINTER_DOWN ");
//
//                downX2 = event.getX(1);
//                downY2 = event.getY(1);
//                distance_last = getFingerSpace(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //注意：即使手指放着不动，ACTION_MOVE也一直在回调
//                switch (event.getPointerCount()) {
//                    case 1:
//                        float mX = event.getX();
//                        float mY = event.getY();
//                        translate_x += mX - downX;
//                        translate_y += mY - downY;
//                        //麻烦
////                    translate_x=Math.max(-getWidth(),Math.min(getWidth(),translate_x));
////                    translate_y=Math.max(-getHeight(),Math.min(getHeight(),translate_y));
//                        invalidate();
//                        downX = mX;
//                        downY = mY;
//                        break;
//                    case 2:
//                        if (enableZoom) {
//                            float distance = getFingerSpace(event);
//                            float dex = distance - distance_last;
//                            zoom += dex / (zoom_max * 50);
//                            zoom = Math.max(zoom_min, Math.min(zoom_max, zoom));
//                            float moveX = event.getX(0);
//                            float moveY = event.getY(0);
//                            float moveX2 = event.getX(1);
//                            float moveY2 = event.getY(1);
//                            translate_x += (moveX + moveX2 - downX - downX2) * 0.5f;
//                            translate_y += (moveY + moveY2 - downY - downY2) * 0.5f;
//                            invalidate();
//                            downX = moveX;
//                            downY = moveY;
//                            downX2 = moveX2;
//                            downY2 = moveY2;
//                            distance_last = distance;
//                        }
//                        if (doubleMoveEventEnable) {
//                            float moveX = event.getX();
//                            float moveY = event.getY();
//                            translate_x += moveX - downX;
//                            translate_y += moveY - downY;
//                            //麻烦
////                    translate_x=Math.max(-getWidth(),Math.min(getWidth(),translate_x));
////                    translate_y=Math.max(-getHeight(),Math.min(getHeight(),translate_y));
//                            invalidate();
//                            downX = moveX;
//                            downY = moveY;
//                        }
//                        break;
//                }
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                LogUtils.log("ACTION_POINTER_UP ");
//
//                //注意：任何一只手指，最先离开屏幕的手指都会回调ACTION_POINTER_UP，
//                //如果是2只手指，如果是第一只手离开导致的回调，getActionIndex是0，如果是第二只，getActionIndex是1
//                if (event.getActionIndex() == 0) {
//                    downX = downX2;
//                    downY = downY2;
//                }
//                event_up();
//                break;
//            case MotionEvent.ACTION_UP:
//                LogUtils.log("ACTION_UP ");
//
//                //如果是2只手指，最后离开的手指回调ACTION_UP，getActionIndex永远是0
//                event_up();
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
//
//    private void event_up() {
//        if (touchEventUpReset) {
//            zoom = 1;
//            translate_x = 0;
//            translate_y = 0;
//            invalidate();
//        }
//        if (callback != null) callback.onActionUp();
//    }
//
//    private float getFingerSpace(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//
//    public float getZoom_max() {
//        return zoom_max;
//    }
//
//    public void setZoom_max(float zoom_max) {
//        this.zoom_max = zoom_max;
//    }
//
//    public float getZoom_min() {
//        return zoom_min;
//    }
//
//    public void setZoom_min(float zoom_min) {
//        this.zoom_min = zoom_min;
//    }
//
//    public float getZoom() {
//        return zoom;
//    }
//
//    public float getTranslate_x() {
//        return translate_x;
//    }
//
//    public float getTranslate_y() {
//        return translate_y;
//    }
//
//    public boolean isEnableZoom() {
//        return enableZoom;
//    }
//
//    public void setEnableZoom(boolean enableZoom) {
//        this.enableZoom = enableZoom;
//    }
//
//    public boolean isDoubleMoveEventEnable() {
//        return doubleMoveEventEnable;
//    }
//
//    public void setDoubleMoveEventEnable(boolean doubleMoveEventEnable) {
//        this.doubleMoveEventEnable = doubleMoveEventEnable;
//    }
//
//    public boolean isTouchEventDownReturnTrue() {
//        return touchEventDownReturnTrue;
//    }
//
//    public void setTouchEventDownReturnTrue(boolean touchEventDownReturnTrue) {
//        this.touchEventDownReturnTrue = touchEventDownReturnTrue;
//    }
//
//    public boolean isTouchEventUpReset() {
//        return touchEventUpReset;
//    }
//
//    public void setTouchEventUpReset(boolean touchEventUpReset) {
//        this.touchEventUpReset = touchEventUpReset;
//    }
//
//    public boolean isSingleMoveEventEnable() {
//        return singleMoveEventEnable;
//    }
//
//    public void setSingleMoveEventEnable(boolean singleMoveEventEnable) {
//        this.singleMoveEventEnable = singleMoveEventEnable;
//    }
//
//    public static interface Callback {
//        public void onCanvasChange(float scale, float dx, float dy);
//
//        public void onActionUp();
//    }
//}
//
