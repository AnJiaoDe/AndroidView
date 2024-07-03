package com.cy.androidview.sticker;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;

import androidx.annotation.ColorInt;

import com.cy.androidview.ScreenUtils;
import com.cy.androidview.selectorview.TextViewSelector;

public class Sticker {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_LABEL = 1;
    public static final int TYPE_PIC = 2;
    private int type;
    private String text;
    private float centerX, centerY;
    private RectF rectF_box_normal;
    private RectF rectFCloseRotated, rectFCopyRotated, rectFRotateRotated, rectF3DRotated;
    private RectF rectFBitmapClose, rectFBitmapCopy, rectFBitmapRotate, rectFBitmap3DRotate;
    private float rotationX = 0;
    private float rotationY = 0;
    private float rotationZ = 0;
    private float scale = 1;
    private RectF rectF_text_normal;
    private Context context;
    private Matrix matrix, matrix_invert;
    private Camera camera;
    private Matrix matrix_camera;
    private float[] center_rotated = new float[2];
    private Path path;
    private float[] points_box0, points_box1,
            points_box2, points_box3;
    private Paint paintText;
    private Paint paintRectF;
    private boolean showBox = true;
    private boolean vertical = false;
    private float lineSpace = 0;
    private int textColor;
    private float textSize;
    private float letterSpacing;
    private Paint.Align textAlign;
    private float blur_radius;
    private String pathFont;
    private int style;
    private float shadowRadius;
    private float shadowDx;
    private float shadowDy;
    private @ColorInt int shadowColor;
    private boolean filter=false;

    public Sticker(Context context, int type, String text) {
        this.context = context;
        this.type = type;
        this.text = text;

        matrix = new Matrix();
        matrix_invert = new Matrix();
        camera = new Camera();
        matrix_camera = new Matrix();

        path = new Path();
        rectF_box_normal = new RectF();
        rectFCloseRotated = new RectF();
        rectFCopyRotated = new RectF();
        rectFRotateRotated = new RectF();
        rectF3DRotated = new RectF();

        rectFBitmapClose = new RectF();
        rectFBitmapCopy = new RectF();
        rectFBitmapRotate = new RectF();
        rectFBitmap3DRotate = new RectF();

        points_box0 = new float[2];
        points_box1 = new float[2];
        points_box2 = new float[2];
        points_box3 = new float[2];

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);

        paintRectF = new Paint();
        paintRectF.setStyle(Paint.Style.STROKE);
        paintRectF.setAntiAlias(true);

        setTextColor(Color.WHITE);
        setTextSize(ScreenUtils.sp2px(context, ScreenUtils.spAdapt(context, 18)));
        setBoxColor(Color.WHITE);
        setBoxStrokeWidth(ScreenUtils.dpAdapt(context, 1));
    }

    public boolean isVertical() {
        return vertical;
    }

    public Sticker setVertical(boolean vertical) {
        this.vertical = vertical;
        return this;
    }

    public String getText() {
        return text;
    }

    public Sticker setText(String text) {
        this.text = text;
        return this;
    }

    public float getLineSpace() {
        return lineSpace;
    }

    public Sticker setLineSpace(float lineSpace) {
        this.lineSpace = lineSpace;
        return this;
    }

    /**
     * 如果缩放过大后进行3D旋转，会导致文字框线条错乱，正常现象，因为溢出边界了
     *
     * @param canvas
     * @param stickerAttr
     */
    public void onDraw(Canvas canvas, StickerAttr stickerAttr) {
        switch (type) {
            case TYPE_TEXT:
//     * staticLayout 虽然可以做到自动换行，但是不太支持3D效果，rotationX 60度，文字直接不见了。
                rectF_text_normal = TextUtils.getTextRectF(vertical, lineSpace, paintText, text, centerX, centerY);
//                    canvas.drawRect(rectF_text_normal,paintRectF);
                rectF_box_normal.left = rectF_text_normal.left - stickerAttr.getRadius_menu();
                rectF_box_normal.top = rectF_text_normal.top - stickerAttr.getRadius_menu();
                rectF_box_normal.right = rectF_text_normal.right + stickerAttr.getRadius_menu();
                rectF_box_normal.bottom = rectF_text_normal.bottom + stickerAttr.getRadius_menu();
//                    canvas.drawRect(rectF_box_normal,paintRectF);
                // 保存当前画布状态
                canvas.save();
                matrix.reset();
                // 设置旋转角度
                //默认是围绕(0,0旋转),顺时针旋转
                matrix.postScale(scale, scale, centerX, centerY);
                matrix.postRotate(rotationZ, centerX, centerY);
                matrix.mapPoints(center_rotated, new float[]{centerX, centerY});

                matrix_camera.reset();
                camera.save();
                camera.rotateX(rotationX);
                camera.rotateY(rotationY);
//                     将 Camera 的变换应用到 Matrix 上
                camera.getMatrix(matrix_camera);
                camera.restore();
                //为了使3D旋转围绕文字中心  先将旋转中心移动到（0,0）点，因为Matrix总是用0,0点作为旋转点，旋转之后将视图放回原来的位置。
                matrix_camera.preTranslate(-center_rotated[0], -center_rotated[1]);
                matrix_camera.postTranslate(center_rotated[0], center_rotated[1]);
                matrix.postConcat(matrix_camera);

                canvas.setMatrix(matrix);
                TextUtils.drawText(vertical, lineSpace, canvas, paintText, text, centerX, centerY, rectF_text_normal);
                canvas.restore();
                //除了文字以外，其他的粗细都应该不进行缩放
                matrix.mapPoints(points_box0, new float[]{rectF_box_normal.left, rectF_box_normal.top});
                matrix.mapPoints(points_box1, new float[]{rectF_box_normal.right, rectF_box_normal.top});
                matrix.mapPoints(points_box2, new float[]{rectF_box_normal.right, rectF_box_normal.bottom});
                matrix.mapPoints(points_box3, new float[]{rectF_box_normal.left, rectF_box_normal.bottom});

                if (!showBox) return;

                path.reset();
                path.moveTo(points_box0[0], points_box0[1]);
                path.lineTo(points_box1[0], points_box1[1]);
                path.lineTo(points_box2[0], points_box2[1]);
                path.lineTo(points_box3[0], points_box3[1]);
                path.close();
                canvas.drawPath(path, paintRectF);

                canvas.drawCircle(points_box0[0], points_box0[1], stickerAttr.getRadius_menu(), stickerAttr.getPaintMenuBg());
                canvas.drawCircle(points_box1[0], points_box1[1], stickerAttr.getRadius_menu(), stickerAttr.getPaintMenuBg());
                canvas.drawCircle(points_box2[0], points_box2[1], stickerAttr.getRadius_menu(), stickerAttr.getPaintMenuBg());
                canvas.drawCircle(points_box3[0], points_box3[1], stickerAttr.getRadius_menu(), stickerAttr.getPaintMenuBg());

                //扩大触摸范围
                rectFCloseRotated.left = points_box0[0] - stickerAttr.getRadius_menu();
                rectFCloseRotated.top = points_box0[1] - stickerAttr.getRadius_menu();
                rectFCloseRotated.right = points_box0[0] + stickerAttr.getRadius_menu();
                rectFCloseRotated.bottom = points_box0[1] + stickerAttr.getRadius_menu();
                rectFBitmapClose.left = points_box0[0] - stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapClose.top = points_box0[1] - stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapClose.right = points_box0[0] + stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapClose.bottom = points_box0[1] + stickerAttr.getRadius_menu() * 0.5f;
                canvas.drawBitmap(stickerAttr.getBitmap_close(), null, rectFBitmapClose, stickerAttr.getPaintBitmap());

                //扩大触摸范围
                rectFCopyRotated.left = points_box1[0] - stickerAttr.getRadius_menu();
                rectFCopyRotated.top = points_box1[1] - stickerAttr.getRadius_menu();
                rectFCopyRotated.right = points_box1[0] + stickerAttr.getRadius_menu();
                rectFCopyRotated.bottom = points_box1[1] + stickerAttr.getRadius_menu();
                rectFBitmapCopy.left = points_box1[0] - stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapCopy.top = points_box1[1] - stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapCopy.right = points_box1[0] + stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapCopy.bottom = points_box1[1] + stickerAttr.getRadius_menu() * 0.5f;
                canvas.drawBitmap(stickerAttr.getBitmap_copy(), null, rectFBitmapCopy, stickerAttr.getPaintBitmap());

                //扩大触摸范围
                rectFRotateRotated.left = points_box2[0] - stickerAttr.getRadius_menu();
                rectFRotateRotated.top = points_box2[1] - stickerAttr.getRadius_menu();
                rectFRotateRotated.right = points_box2[0] + stickerAttr.getRadius_menu();
                rectFRotateRotated.bottom = points_box2[1] + stickerAttr.getRadius_menu();
                rectFBitmapRotate.left = points_box2[0] - stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapRotate.top = points_box2[1] - stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapRotate.right = points_box2[0] + stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmapRotate.bottom = points_box2[1] + stickerAttr.getRadius_menu() * 0.5f;
                canvas.drawBitmap(stickerAttr.getBitmap_rotate(), null, rectFBitmapRotate, stickerAttr.getPaintBitmap());

                //扩大触摸范围
                rectF3DRotated.left = points_box3[0] - stickerAttr.getRadius_menu();
                rectF3DRotated.top = points_box3[1] - stickerAttr.getRadius_menu();
                rectF3DRotated.right = points_box3[0] + stickerAttr.getRadius_menu();
                rectF3DRotated.bottom = points_box3[1] + stickerAttr.getRadius_menu();
                rectFBitmap3DRotate.left = points_box3[0] - stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmap3DRotate.top = points_box3[1] - stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmap3DRotate.right = points_box3[0] + stickerAttr.getRadius_menu() * 0.5f;
                rectFBitmap3DRotate.bottom = points_box3[1] + stickerAttr.getRadius_menu() * 0.5f;
                canvas.drawBitmap(stickerAttr.getBitmap_rotate_3d(), null, rectFBitmap3DRotate, stickerAttr.getPaintBitmap());
                break;
        }
    }

    public Sticker setTypeface(String pathFont, int style) {
        this.pathFont = pathFont;
        this.style = style;
        if (android.text.TextUtils.isEmpty(pathFont)) {
            paintText.setTypeface(Typeface.defaultFromStyle(style));
            return this;
        }
        paintText.setTypeface(
                Typeface.create(Typeface.createFromFile(pathFont), style));
        return this;
    }

    public String getPathFont() {
        return pathFont;
    }

    public int getStyle() {
        return style;
    }

    public boolean isShowBox() {
        return showBox;
    }

    public Sticker setShowBox(boolean showBox) {
        this.showBox = showBox;
        return this;
    }


    public Sticker setBoxColor(int color) {
        paintRectF.setColor(color);
        return this;
    }

    public Sticker setBoxStrokeWidth(float px) {
        paintRectF.setStrokeWidth(px);
        return this;
    }

    public float getOneLineHeight() {
        return TextUtils.getTextHeight(paintText);
    }

    public Sticker setFilterBitmap(boolean filter) {
        this.filter=filter;
        paintText.setFilterBitmap(filter);
        return this;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public Matrix getMatrix_invert() {
        matrix.invert(matrix_invert);
        return matrix_invert;
    }

    public Camera getCamera() {
        return camera;
    }

    public Matrix getMatrix_camera() {
        return matrix_camera;
    }


    public int getType() {
        return type;
    }

    public Sticker setType(int type) {
        this.type = type;
        return this;
    }

    public int getTextColor() {
        return textColor;
    }

    public Sticker setTextColor(int textColor) {
        this.textColor = textColor;
        paintText.setColor(textColor);
        return this;
    }

    public float getTextSize() {
        return textSize;
    }

    public Sticker setTextSize(float textSize) {
        this.textSize = textSize;
        paintText.setTextSize(textSize);
        return this;
    }

    public float getLetterSpacing() {
        return letterSpacing;
    }

    public Sticker setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
        paintText.setLetterSpacing(letterSpacing);
        return this;
    }

    public Paint.Align getTextAlign() {
        return textAlign;
    }

    public Sticker setTextAlign(Paint.Align textAlign) {
        this.textAlign = textAlign;
        paintText.setTextAlign(textAlign);
        return this;
    }

    public float getRotationX() {
        return rotationX;
    }

    public Sticker setRotationX(float rotationX) {
        this.rotationX = rotationX;
        return this;
    }

    public float getRotationY() {
        return rotationY;
    }

    public Sticker setRotationY(float rotationY) {
        this.rotationY = rotationY;
        return this;
    }

    public float getCenterX() {
        return centerX;
    }

    public Sticker setCenterX(float centerX) {
        this.centerX = centerX;
        return this;
    }

    public float getCenterY() {
        return centerY;
    }

    public Sticker setCenterY(float centerY) {
        this.centerY = centerY;
        return this;
    }


    public RectF getRectF_box_normal() {
        return rectF_box_normal;
    }

//    public Sticker setRectF_box_normal(RectF rectF_box_normal) {
//        this.rectF_box_normal = rectF_box_normal;
//        return this;
//    }

    public RectF getRectFCloseRotated() {
        return rectFCloseRotated;
    }

//    public Sticker setRectFCloseRotated(RectF rectFCloseRotated) {
//        this.rectFCloseRotated = rectFCloseRotated;
//        return this;
//    }

    public RectF getRectF3DRotated() {
        return rectF3DRotated;
    }

//    public Sticker setRectF3DRotated(RectF rectF3DRotated) {
//        this.rectF3DRotated = rectF3DRotated;
//        return this;
//    }

    public RectF getRectFRotateRotated() {
        return rectFRotateRotated;
    }

//    public Sticker setRectFRotateRotated(RectF rectFRotateRotated) {
//        this.rectFRotateRotated = rectFRotateRotated;
//        return this;
//    }

    public RectF getRectFCopyRotated() {
        return rectFCopyRotated;
    }

//    public Sticker setRectFCopyRotated(RectF rectFCopyRotated) {
//        this.rectFCopyRotated = rectFCopyRotated;
//        return this;
//    }

    public float getRotationZ() {
        return rotationZ;
    }

    public Sticker setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
        return this;
    }

    public float getScale() {
        return scale;
    }

    public Sticker setScale(float scale) {
        this.scale = scale;
        return this;
    }

//    public RectF getRectF_text_normal() {
//        return rectF_text_normal;
//    }
//
//    public Sticker setRectF_text_normal(RectF rectF_text_normal) {
//        this.rectF_text_normal = rectF_text_normal;
//        return this;
//    }
//
//    public RectF getRectFBitmapClose() {
//        return rectFBitmapClose;
//    }
//
//    public Sticker setRectFBitmapClose(RectF rectFBitmapClose) {
//        this.rectFBitmapClose = rectFBitmapClose;
//        return this;
//    }
//
//    public RectF getRectFBitmapCopy() {
//        return rectFBitmapCopy;
//    }
//
//    public Sticker setRectFBitmapCopy(RectF rectFBitmapCopy) {
//        this.rectFBitmapCopy = rectFBitmapCopy;
//        return this;
//    }
//
//    public RectF getRectFBitmapRotate() {
//        return rectFBitmapRotate;
//    }
//
//    public Sticker setRectFBitmapRotate(RectF rectFBitmapRotate) {
//        this.rectFBitmapRotate = rectFBitmapRotate;
//        return this;
//    }
//
//    public RectF getRectFBitmap3DRotate() {
//        return rectFBitmap3DRotate;
//    }
//
//    public Sticker setRectFBitmap3DRotate(RectF rectFBitmap3DRotate) {
//        this.rectFBitmap3DRotate = rectFBitmap3DRotate;
//        return this;
//    }
//
//    public Sticker setMatrix(Matrix matrix) {
//        this.matrix = matrix;
//        return this;
//    }
//
//    public Sticker setMatrix_invert(Matrix matrix_invert) {
//        this.matrix_invert = matrix_invert;
//        return this;
//    }
//
//    public Sticker setCamera(Camera camera) {
//        this.camera = camera;
//        return this;
//    }
//
//    public Sticker setMatrix_camera(Matrix matrix_camera) {
//        this.matrix_camera = matrix_camera;
//        return this;
//    }
//
//    public float[] getCenter_rotated() {
//        return center_rotated;
//    }
//
//    public Sticker setCenter_rotated(float[] center_rotated) {
//        this.center_rotated = center_rotated;
//        return this;
//    }
//
//    public Path getPath() {
//        return path;
//    }
//
//    public Sticker setPath(Path path) {
//        this.path = path;
//        return this;
//    }
//
//    public float[] getPoints_box0() {
//        return points_box0;
//    }
//
//    public Sticker setPoints_box0(float[] points_box0) {
//        this.points_box0 = points_box0;
//        return this;
//    }
//
//    public float[] getPoints_box1() {
//        return points_box1;
//    }
//
//    public void setPoints_box1(float[] points_box1) {
//        this.points_box1 = points_box1;
//    }
//
//    public float[] getPoints_box2() {
//        return points_box2;
//    }
//
//    public void setPoints_box2(float[] points_box2) {
//        this.points_box2 = points_box2;
//    }
//
//    public float[] getPoints_box3() {
//        return points_box3;
//    }
//
//    public void setPoints_box3(float[] points_box3) {
//        this.points_box3 = points_box3;
//    }

    public float getBlur_radius() {
        return blur_radius;
    }

    /**
     * @param blur_radius 必须>0 否则崩溃
     */
    public Sticker setMaskFilter(float blur_radius) {
        blur_radius=Math.max(1,blur_radius);
        this.blur_radius = blur_radius;
        paintText.setMaskFilter(new BlurMaskFilter(blur_radius, BlurMaskFilter.Blur.SOLID));
        return this;
    }

    public Sticker setShadowLayer(float radius, float dx, float dy, @ColorInt int shadowColor) {
        this.shadowRadius = radius;
        this.shadowDx = dx;
        this.shadowDy = dy;
        this.shadowColor = shadowColor;
        paintText.setShadowLayer(radius, dx, dy, shadowColor);
        return this;
    }

    /**
     * 注意：这里不要复制类的对象，否则容易出现共用错误，
     *
     * @return
     */
    public Sticker copy() {
        Sticker sticker = new Sticker(context, type, text);

        sticker.setCenterX(centerX);
        sticker.setCenterY(centerY);
        sticker.setRotationX(rotationX);
        sticker.setRotationY(rotationY);
        sticker.setRotationZ(rotationZ);
        sticker.setScale(scale);
        sticker.setTextColor(paintText.getColor());
        sticker.setTextSize(paintText.getTextSize());
        sticker.setLetterSpacing(paintText.getLetterSpacing());
        sticker.setTextAlign(paintText.getTextAlign());
        sticker.setMaskFilter(blur_radius);
        sticker.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        sticker.setTypeface(pathFont, style);
        sticker.setVertical(vertical);
        sticker.setLineSpace(lineSpace);
        sticker.setFilterBitmap(filter);

        sticker.setBoxColor(paintRectF.getColor());
        sticker.setBoxStrokeWidth(paintRectF.getStrokeWidth());

        return sticker;
    }
}
