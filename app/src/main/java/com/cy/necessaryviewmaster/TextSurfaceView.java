package com.cy.necessaryviewmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TextSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Thread drawThread;
    private boolean isDrawing = false;

    public TextSurfaceView(Context context) {
        this(context,null);
    }

    public TextSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isDrawing) {
                    drawTextOnSurface(holder);
                    try {
                        Thread.sleep(100); // 控制绘制频率
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 可以在此做一些其他处理
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
        try {
            drawThread.join();  // 等待线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void drawTextOnSurface(SurfaceHolder holder) {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas(); // 锁定画布，获取绘制权限
            if (canvas != null) {
                // 清除画布
                canvas.drawColor(Color.WHITE);

                // 创建画笔对象
                Paint paint = new Paint();
                paint.setColor(Color.BLACK); // 设置文字颜色
                paint.setTextSize(100); // 设置文字大小
                paint.setAntiAlias(true); // 开启抗锯齿

                // 在 Canvas 上绘制文字
                canvas.drawText("Hello, SurfaceView!", 100, 200, paint);
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas); // 解锁画布并提交绘制内容
            }
        }
    }
}
