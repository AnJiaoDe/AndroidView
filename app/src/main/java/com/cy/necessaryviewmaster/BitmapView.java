package com.cy.necessaryviewmaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.cy.androidview.BitmapUtils;
import com.cy.androidview.sticker.TextUtils;

public class BitmapView extends View {
    private Bitmap bitmap;
    private Matrix matrix;
    private Camera camera;
    private Matrix matrix_camera;
    private Paint paint;
    private float centerX, centerY;
    private float rotationX = 0;
    private float rotationY =30 ;
    private float rotationZ = 0;
    private float scale = 0.08f;
    private float[] center_rotated = new float[2];

    public BitmapView(Context context) {
        this(context, null);
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = BitmapUtils.decodeResourceOrigin(context, R.drawable.pic_big);
        matrix = new Matrix();
        camera = new Camera();
        matrix_camera = new Matrix();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getWidth() * 0.5f;
        centerY = getHeight() * 0.5f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 保存当前画布状态
        canvas.save();
        matrix.reset();
        // 设置旋转角度
        //默认是围绕(0,0旋转),顺时针旋转
        matrix.postScale(scale, scale, 0, 0);
        paint.setColor(Color.GREEN);
        canvas.setMatrix(matrix);
        canvas.drawRect(new RectF(0,0,bitmap.getWidth(),bitmap.getHeight()),paint);
        canvas.restore();

        canvas.save();
//        matrix.postRotate(rotationZ, centerX, centerY);

        matrix_camera.reset();
        camera.save();
        camera.rotateX(rotationX);
        camera.rotateY(rotationY);
        //正数逆时针
        camera.rotateZ(rotationZ);
//                     将 Camera 的变换应用到 Matrix 上
        camera.getMatrix(matrix_camera);
        camera.restore();


        matrix.postTranslate(-bitmap.getWidth()*0.5f*scale, -bitmap.getHeight()*0.5f*scale);
        matrix.postConcat(matrix_camera);
        matrix.postTranslate(bitmap.getWidth()*0.5f*scale, bitmap.getHeight()*0.5f*scale);

//        matrix.mapPoints(center_rotated, new float[]{centerX, centerY});
//        matrix.postTranslate(bitmap.getWidth()*0.5f, bitmap.getHeight()*0.5f);

//        matrix.postTranslate(centerX-bitmap.getWidth()*0.5f,centerY-bitmap.getHeight()*0.5f);

//        matrix.mapPoints(center_rotated, new float[]{bitmap.getWidth()*0.5f, bitmap.getHeight()*0.5f});
        //为了使3D旋转围绕文字中心  先将旋转中心移动到（0,0）点，因为Matrix总是用0,0点作为旋转点，旋转之后将视图放回原来的位置。
//        matrix.postTranslate(bitmap.getWidth()*0.5f-center_rotated[0], bitmap.getHeight()*0.5f- center_rotated[1]);

//        matrix.mapPoints(center_rotated, new float[]{centerX, centerY});
//        matrix.postTranslate(centerX-bitmap.getWidth()*0.5f, centerY-bitmap.getHeight()*0.5f);

//        canvas.setMatrix(matrix);
        //限定位置，否则会出屏
        canvas.drawBitmap(bitmap,matrix,paint);
        canvas.restore();
    }
    public RectF getPicRectF(float centerX, float centerY) {
        if (bitmap == null || bitmap.isRecycled()) return new RectF(0, 0, 0, 0);
        return new RectF(centerX - bitmap.getWidth() * 0.5f, centerY - bitmap.getHeight() * 0.5f,
                centerX + bitmap.getWidth() * 0.5f, centerY + bitmap.getHeight() * 0.5f);
    }
}
