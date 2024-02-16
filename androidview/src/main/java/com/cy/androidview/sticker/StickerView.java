package com.cy.androidview.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.LogUtils;
import com.cy.androidview.R;
import com.cy.androidview.ScreenUtils;
import com.cy.androidview.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class StickerView extends View {
    private List<Sticker> listSticker;
    private Paint paintMenuBg, paintBitmap;
    private int index_rotateZ = -1;
    private int index_rotate3D = -1;
    private int index_down = -1;
    private int radius_menu;
    private Matrix matrix;
    private Path path;
    private float[] points_box0 = new float[2], points_box1 = new float[2],
            points_box2 = new float[2], points_box3 = new float[2],
            points_box_all = new float[8];
    private float downX, downY, moveX_last, moveY_last;
    private float[] center_rotated = new float[2];
    private long downTime;
    private final int DOWN_CLOSE = 0;
    private final int DOWN_COPY = 1;
    private final int DOWN_BOX = 2;
    private int downIn = -1;
    private Callback callback;
    private final int TIME_CLICK_THRESHOLD = 200;

    private Camera camera;
    private Matrix matrix_camera;
    private Bitmap bitmap_close, bitmap_copy, bitmap_rotate, bitmap_rotate_3d;

    public StickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        listSticker = new ArrayList<>();

        paintMenuBg = new Paint();
        paintMenuBg.setAntiAlias(true);

        paintBitmap = new Paint();
        paintBitmap.setAntiAlias(true);

        matrix = new Matrix();
        path = new Path();
        camera = new Camera();
        matrix_camera = new Matrix();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StickerView);
        setMenuBgColor(typedArray.getColor(R.styleable.StickerView_cy_color_bg_menu, Color.BLACK));
        setRadius_menu(typedArray.getDimensionPixelSize(R.styleable.StickerView_cy_radius_menu, ScreenUtils.dpAdapt(context, 10)));

        bitmap_close = BitmapUtils.decodeResourceOrigin(context,
                typedArray.getResourceId(R.styleable.StickerView_cy_src_close, R.drawable.close_white));
        bitmap_copy = BitmapUtils.decodeResourceOrigin(context,
                typedArray.getResourceId(R.styleable.StickerView_cy_src_copy, R.drawable.copy_white));
        bitmap_rotate = BitmapUtils.decodeResourceOrigin(context,
                typedArray.getResourceId(R.styleable.StickerView_cy_src_rotateZ, R.drawable.rotate_white));
        bitmap_rotate_3d = BitmapUtils.decodeResourceOrigin(context,
                typedArray.getResourceId(R.styleable.StickerView_cy_src_rotateXY, R.drawable.rotate_3d_white));

        typedArray.recycle();
    }

    public void setMenuBgColor(int color) {
        paintMenuBg.setColor(color);
    }

    public void setRadius_menu(int radius_menu) {
        this.radius_menu = radius_menu;
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
            switch (sticker.getType()) {
                case Sticker.TYPE_TEXT:
                    // 保存当前画布状态
                    canvas.save();

                    sticker.setBaseLineX(TextUtils.getBaseLineX(sticker.getPaintText(), sticker.getText(), sticker.getCenterX()));
                    sticker.setBaseLineY(TextUtils.getBaseLineY(sticker.getPaintText(), sticker.getCenterY()));
//                    FontMetricsInt 类 有 top 、bottom 两个成员，
//                    top表示基线到文字最上面的位置的距离 是个负值 bottom为基线到最下面的距离，为正值
//                    如果想要基于一个位置竖直居中，那么居中的位置 坐标假设为 centerY
                    RectF rectF_text_normal = TextUtils.getTextRectF(sticker.getText(), sticker.getPaintText(), sticker.getBaseLineX(),
                            sticker.getBaseLineY());
                    sticker.setRectF_text_normal(rectF_text_normal);

                    matrix.reset();
                    // 设置旋转角度
                    //默认是围绕(0,0旋转),顺时针旋转
                    matrix.postRotate(sticker.getRotationZ(), rectF_text_normal.centerX(), rectF_text_normal.centerY());
                    matrix.postScale(sticker.getScale(), sticker.getScale(), rectF_text_normal.centerX(), rectF_text_normal.centerY());
                    canvas.setMatrix(matrix);
                    matrix.mapPoints(center_rotated, new float[]{rectF_text_normal.centerX(), rectF_text_normal.centerY()});

                    matrix_camera.reset();
                    camera.save();
                    camera.rotateX(sticker.getRotationX());
                    camera.rotateY(sticker.getRotationY());
//                     将 Camera 的变换应用到 Matrix 上
                    camera.getMatrix(matrix_camera);
                    camera.restore();
                    //为了使3D旋转围绕文字中心  先将旋转中心移动到（0,0）点，因为Matrix总是用0,0点作为旋转点，旋转之后将视图放回原来的位置。
                    matrix_camera.preTranslate(-center_rotated[0], -center_rotated[1]);
                    matrix_camera.postTranslate(center_rotated[0], center_rotated[1]);
                    matrix.postConcat(matrix_camera);

                    canvas.setMatrix(matrix);
                    canvas.drawText(sticker.getText(), sticker.getBaseLineX(), sticker.getBaseLineY(), sticker.getPaintText());
                    canvas.restore();

                    int padding = (int) (sticker.getPaintText().getTextSize() * 0.4);
                    RectF rectF_box = new RectF();
                    rectF_box.left = rectF_text_normal.left - padding;
                    rectF_box.top = rectF_text_normal.top - padding;
                    rectF_box.right = rectF_text_normal.right + padding;
                    rectF_box.bottom = rectF_text_normal.bottom + padding;
                    sticker.setRectF_box_normal(rectF_box);

                    matrix.mapPoints(points_box0, new float[]{rectF_box.left, rectF_box.top});
                    matrix.mapPoints(points_box1, new float[]{rectF_box.right, rectF_box.top});
                    matrix.mapPoints(points_box2, new float[]{rectF_box.right, rectF_box.bottom});
                    matrix.mapPoints(points_box3, new float[]{rectF_box.left, rectF_box.bottom});
                    points_box_all[0] = points_box0[0];
                    points_box_all[1] = points_box0[1];
                    points_box_all[2] = points_box1[0];
                    points_box_all[3] = points_box1[1];
                    points_box_all[4] = points_box2[0];
                    points_box_all[5] = points_box2[1];
                    points_box_all[6] = points_box3[0];
                    points_box_all[7] = points_box3[1];

                    path.reset();
                    path.moveTo(points_box0[0], points_box0[1]);
                    path.lineTo(points_box1[0], points_box1[1]);
                    path.lineTo(points_box2[0], points_box2[1]);
                    path.lineTo(points_box3[0], points_box3[1]);
                    path.close();
                    canvas.drawPath(path, sticker.getPaintRectF());

                    canvas.drawCircle(points_box0[0], points_box0[1], radius_menu, paintMenuBg);
                    canvas.drawCircle(points_box1[0], points_box1[1], radius_menu, paintMenuBg);
                    canvas.drawCircle(points_box2[0], points_box2[1], radius_menu, paintMenuBg);
                    canvas.drawCircle(points_box3[0], points_box3[1], radius_menu, paintMenuBg);

                    //扩大触摸范围
                    RectF rectF_close = new RectF(points_box0[0] - radius_menu, points_box0[1] - radius_menu,
                            points_box0[0] + radius_menu, points_box0[1] + radius_menu);
                    sticker.setRectFCloseRotated(rectF_close);
                    canvas.drawBitmap(bitmap_close, null,
                            new RectF(points_box0[0] - radius_menu * 0.5f, points_box0[1] - radius_menu * 0.5f,
                                    points_box0[0] + radius_menu * 0.5f, points_box0[1] + radius_menu * 0.5f),
                            paintBitmap);

                    //扩大触摸范围
                    RectF rectF_copy = new RectF(points_box1[0] - radius_menu, points_box1[1] - radius_menu,
                            points_box1[0] + radius_menu, points_box1[1] + radius_menu);
                    sticker.setRectFCopyRotated(rectF_copy);
                    canvas.drawBitmap(bitmap_copy, null,
                            new RectF(points_box1[0] - radius_menu * 0.5f, points_box1[1] - radius_menu * 0.5f,
                                    points_box1[0] + radius_menu * 0.5f, points_box1[1] + radius_menu * 0.5f),
                            paintBitmap);

                    //扩大触摸范围
                    RectF rectF_rotate = new RectF(points_box2[0] - radius_menu, points_box2[1] - radius_menu,
                            points_box2[0] + radius_menu, points_box2[1] + radius_menu);
                    sticker.setRectFRotateRotated(rectF_rotate);
                    canvas.drawBitmap(bitmap_rotate, null,
                            new RectF(points_box2[0] - radius_menu * 0.5f, points_box2[1] - radius_menu * 0.5f,
                                    points_box2[0] + radius_menu * 0.5f, points_box2[1] + radius_menu * 0.5f),
                            paintBitmap);

                    //扩大触摸范围
                    RectF rectF_3D = new RectF(points_box3[0] - radius_menu, points_box3[1] - radius_menu,
                            points_box3[0] + radius_menu, points_box3[1] + radius_menu);
                    sticker.setRectF3DRotated(rectF_3D);
                    canvas.drawBitmap(bitmap_rotate_3d, null,
                            new RectF(points_box3[0] - radius_menu * 0.5f, points_box3[1] - radius_menu * 0.5f,
                                    points_box3[0] + radius_menu * 0.5f, points_box3[1] + radius_menu * 0.5f),
                            paintBitmap);
                    break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();//float DownX
                downY = event.getY();//float DownY
                moveX_last = downX;
                moveY_last = downY;
                downTime = System.currentTimeMillis();//long currentMS     获取系统时间

                //注意：应该倒叙遍历，因为后添加的在上层，
                for (int i = listSticker.size() - 1; i >= 0; i--) {
                    Sticker sticker = listSticker.get(i);
                    //一定要先判断旋转和点击再判断移动，否则容易导致文本框被移出view之外
                    if (sticker.getRectFRotateRotated().contains((int) event.getX(), (int) event.getY())) {
                        index_rotateZ = i;
                        break;
                    }
                    if (sticker.getRectF3DRotated().contains((int) event.getX(), (int) event.getY())) {
                        index_rotate3D = i;
                        break;
                    }
                    if (sticker.getRectFCloseRotated().contains((int) event.getX(), (int) event.getY())) {
                        index_down = i;
                        downIn = DOWN_CLOSE;
                        break;
                    }
                    if (sticker.getRectFCopyRotated().contains((int) event.getX(), (int) event.getY())) {
                        index_down = i;
                        downIn = DOWN_COPY;
                        break;
                    }
                    matrix.reset();
                    // 设置旋转角度
                    //默认是围绕(0,0旋转),顺时针旋转
                    matrix.postRotate(-sticker.getRotationZ(),
                            sticker.getRectF_text_normal().centerX(), sticker.getRectF_text_normal().centerY());
                    matrix.postScale(1 / sticker.getScale(), 1 / sticker.getScale(),
                            sticker.getRectF_text_normal().centerX(), sticker.getRectF_text_normal().centerY());
                    float[] points_touch_back = new float[2];
                    matrix.mapPoints(points_touch_back, new float[]{event.getX(), event.getY()});

                    if (sticker.getRectF_box_normal().contains(points_touch_back[0], points_touch_back[1])) {
                        index_down = i;
                        downIn = DOWN_BOX;
                        break;
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
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
                } else if (index_rotate3D >= 0 && index_rotate3D < listSticker.size()) {
                    Sticker sticker = listSticker.get(index_rotate3D);

                    float dx = event.getX() - moveX_last;
                    float dy = event.getY() - moveY_last;
                    moveX_last = event.getX();
                    moveY_last = event.getY();

                    sticker.setRotationX(sticker.getRotationX() - dy);
                    sticker.setRotationY(sticker.getRotationY() + dx);

                    invalidate();
                } else if (index_down >= 0 && index_down < listSticker.size() && System.currentTimeMillis() - downTime > TIME_CLICK_THRESHOLD) {
                    Sticker sticker = listSticker.get(index_down);
                    sticker.setCenterX(Math.min(Math.max(0, event.getX()), getWidth()));
                    sticker.setCenterY(Math.min(Math.max(0, event.getY()), getHeight()));

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - downTime < TIME_CLICK_THRESHOLD &&
                        Math.abs(event.getX() - downX) < 10 && Math.abs(event.getY() - downY) < 10) {
                    switch (downIn) {
                        case DOWN_BOX:
                            invalidate();
                            if (callback != null) callback.onBoxClick(index_down);
                            break;
                        case DOWN_CLOSE:
                            listSticker.remove(index_down);
                            invalidate();
                            if (callback != null) callback.onCloseClick(index_down);
                            break;
                        case DOWN_COPY:
                            listSticker.add(listSticker.get(index_down).cloneDeep());
                            invalidate();
                            if (callback != null) callback.onCopyClick(index_down);
                            break;
                    }
                }
                index_rotateZ = -1;
                index_rotate3D = -1;
                index_down = -1;
                downIn = -1;
                break;
        }
        return super.onTouchEvent(event);
    }

    public static interface Callback {
        public void onBoxClick(int index);

        public void onCloseClick(int index);

        public void onCopyClick(int index);
    }
}
