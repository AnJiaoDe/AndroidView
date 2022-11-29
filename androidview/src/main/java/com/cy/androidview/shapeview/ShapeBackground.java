package com.cy.androidview.shapeview;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.StyleableRes;

import com.cy.androidview.R;
import com.cy.androidview.rippleview.Ripple;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/26 22:59
 * @UpdateUser:
 * @UpdateDate: 2020/6/26 22:59
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ShapeBackground {
    private TypedArray typedArray;
    private View view;
    private int radiusCorner = 0;
    private int radiusTopLeft = 0;
    private int radiusTopRight = 0;
    private int radiusBottomRight = 0;
    private int radiusBottomLeft = 0;
    private int colorFill = 0x00000000;
    private int radiusGradient = 0;
    private int colorStart = 0x00000000;
//    private int colorCenter = 0x00000000;
    private int colorEnd = 0x00000000;
    private int orientationGradient = 6;
    private int gradientType = 0;
    private float centerX = 0.5f;
    private float centerY = 0.5f;
    private int angle = 0;
    private int strokeWidth = 0;
    private int strokeColor = 0x00000000;
    private int strokeDashWidth = 0;
    private int strokeDashGap = 0;
    private int strokePaddingLeft = 0;
    private int strokePaddingTop = 0;
    private int strokePaddingRight = 0;
    private int strokePaddingBottom = 0;
    private int shapeType = 0;
    private GradientDrawable gradientDrawable;

    public ShapeBackground(View view,TypedArray typedArray) {
        this.view = view;
        this.typedArray = typedArray;
        this.radiusCorner = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_radiusCorner, radiusCorner);
        this.radiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_radiusTopLeft, radiusTopLeft);
        this.radiusTopRight = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_radiusTopRight, radiusTopRight);
        this.radiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_radiusBottomRight, radiusBottomRight);
        this.radiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_radiusBottomLeft, radiusBottomLeft);
        this.colorFill = typedArray.getColor(R.styleable.AttrsShape_cy_colorFill, colorFill);
        this.colorStart = typedArray.getColor(R.styleable.AttrsShape_cy_colorStart, colorStart);
//        this.colorCenter = typedArray.getColor(R.styleable.AttrsShape_cy_colorCenter, colorCenter);
        this.colorEnd = typedArray.getColor(R.styleable.AttrsShape_cy_colorEnd, colorEnd);
        this.orientationGradient = typedArray.getInt(R.styleable.AttrsShape_cy_orientationGradient, orientationGradient);
        this.gradientType = typedArray.getInt(R.styleable.AttrsShape_cy_gradientType, gradientType);
        this.centerX = typedArray.getFloat(R.styleable.AttrsShape_cy_centerX, centerX);
        this.centerY = typedArray.getFloat(R.styleable.AttrsShape_cy_centerY, centerY);
        this.angle = typedArray.getInt(R.styleable.AttrsShape_cy_angle, angle);
        this.strokeWidth = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_strokeWidth, strokeWidth);
        this.strokeColor = typedArray.getColor(R.styleable.AttrsShape_cy_strokeColor, strokeColor);
        this.strokeDashWidth = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_strokeDashWidth, strokeDashWidth);
        this.strokeDashGap = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_strokeDashGap, strokeDashGap);
        this.strokePaddingLeft = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_strokePaddingLeft, strokePaddingLeft);
        this.strokePaddingTop = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_strokePaddingTop, strokePaddingTop);
        this.strokePaddingRight = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_strokePaddingRight, strokePaddingRight);
        this.strokePaddingBottom = typedArray.getDimensionPixelSize(R.styleable.AttrsShape_cy_strokePaddingBottom, strokePaddingBottom);
        this.shapeType = typedArray.getInt(R.styleable.AttrsShape_cy_shapeType, shapeType);

    }

    public int getRadiusCorner() {
        return radiusCorner;
    }

    public ShapeBackground setRadiusCorner_(int radiusCorner) {
        this.radiusCorner = radiusCorner;
        return this;
    }

    public int getRadiusTopLeft() {
        return radiusTopLeft;
    }

    public ShapeBackground setRadiusTopLeft_(int radiusTopLeft) {
        this.radiusTopLeft = radiusTopLeft;
        return this;

    }

    public int getRadiusTopRight() {
        return radiusTopRight;
    }

    public ShapeBackground setRadiusTopRight_(int radiusTopRight) {
        this.radiusTopRight = radiusTopRight;
        return this;

    }

    public int getRadiusBottomRight() {
        return radiusBottomRight;
    }

    public ShapeBackground setRadiusBottomRight_(int radiusBottomRight) {
        this.radiusBottomRight = radiusBottomRight;
        return this;

    }

    public int getRadiusBottomLeft() {
        return radiusBottomLeft;
    }

    public ShapeBackground setRadiusBottomLeft_(int radiusBottomLeft ) {
        this.radiusBottomLeft = radiusBottomLeft;
        return this;

    }

    public int getColorFill() {
        return colorFill;
    }
    public ShapeBackground setColorFill_(int colorFill) {
        this.colorFill = colorFill;
        return this;

    }

    public int getRadiusGradient() {
        return radiusGradient;
    }

    public ShapeBackground setRadiusGradient_(int radiusGradient) {
        this.radiusGradient = radiusGradient;
        return this;

    }

    public int getColorStart() {
        return colorStart;
    }

    public ShapeBackground setColorStart_(int colorStart) {
        this.colorStart =colorStart;
        return this;

    }

//    public int getColorCenter() {
//        return colorCenter;
//    }

//    public ShapeBackground setColorCenter_(int colorCenter) {
//        this.colorCenter =colorCenter;
//        return this;
//    }

    public int getColorEnd() {
        return colorEnd;
    }

    public ShapeBackground setColorEnd_(int colorEnd) {
        this.colorEnd = colorEnd;
        return this;

    }

    public int getOrientationGradient() {
        return orientationGradient;
    }

    public ShapeBackground setOrientationGradient_(int orientationGradient) {
        this.orientationGradient = orientationGradient;
        return this;

    }

    public int getGradientType() {
        return gradientType;
    }
    public ShapeBackground setGradientType_(int gradientType) {
        this.gradientType = gradientType;
        return this;

    }

    public float getCenterX() {
        return centerX;
    }

    public ShapeBackground setCenterX_(int centerX ) {
        this.centerX = centerX;
        return this;

    }

    public float getCenterY() {
        return centerY;
    }

    public ShapeBackground setCenterY_(int centerY) {
        this.centerY = centerY;
        return this;

    }

    public int getAngle() {
        return angle;
    }
    public ShapeBackground setAngle_(int angle) {
        this.angle = angle;
        return this;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public ShapeBackground setStrokeWidth_(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public ShapeBackground setStrokeColor_(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public int getStrokeDashWidth() {
        return strokeDashWidth;
    }

    public ShapeBackground setStrokeDashWidth_(int strokeDashWidth) {
        this.strokeDashWidth =strokeDashWidth;
        return this;
    }

    public int getStrokeDashGap() {
        return strokeDashGap;
    }

    public ShapeBackground setStrokeDashGap_(int strokeDashGap) {
        this.strokeDashGap = strokeDashGap;
        return this;
    }

    public int getStrokePaddingLeft() {
        return strokePaddingLeft;
    }

    public ShapeBackground setStrokePaddingLeft_(int strokePaddingLeft) {
        this.strokePaddingLeft = strokePaddingLeft;
        return this;
    }

    public int getStrokePaddingTop() {
        return strokePaddingTop;
    }

    public ShapeBackground setStrokePaddingTop_(int strokePaddingTop) {
        this.strokePaddingTop =strokePaddingTop;
        return this;
    }

    public int getStrokePaddingRight() {
        return strokePaddingRight;
    }

    public ShapeBackground setStrokePaddingRight_(int strokePaddingRight) {
        this.strokePaddingRight = strokePaddingRight;
        return this;
    }

    public int getStrokePaddingBottom() {
        return strokePaddingBottom;
    }

    public ShapeBackground setStrokePaddingBottom_(int strokePaddingBottom) {
        this.strokePaddingBottom = strokePaddingBottom;
        return this;
    }

    public int getShapeType() {
        return shapeType;
    }

    public ShapeBackground setShapeType_(int shapeType) {
        this.shapeType = shapeType;
        return this;
    }

    public ShapeBackground shape() {
//        //设置了填充色或者设置了渐变色的开始和结束，或者设置了描边颜色，才会设置drawable
        if (colorFill == 0x00000000 && strokeColor == 0x00000000 && (colorStart == 0x00000000 || colorEnd == 0x00000000))
            return this;

        //创建背景drawable
        gradientDrawable = new GradientDrawable();
        //形状类型
        switch (shapeType) {
            case 0:
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);//矩形
                //，左上角开始，顺时针到左下角，1.左上角x方向弧度，2.左上角y方向弧度，3.右上角x方向弧度，4.右下角y方向弧度，以此类推
                float[] cornerRadii = {radiusTopLeft, radiusTopLeft, radiusTopRight, radiusTopRight,
                        radiusBottomRight, radiusBottomRight, radiusBottomLeft, radiusBottomLeft};
                gradientDrawable.setCornerRadii(cornerRadii);//设置四个角的8个弧度半径
                if (radiusCorner != 0)
                    gradientDrawable.setCornerRadius(radiusCorner);//radiusCorner优先级比cornerRadii高
                break;
            case 1:
                gradientDrawable.setShape(GradientDrawable.OVAL);//椭圆

                break;
            case 2:
                gradientDrawable.setShape(GradientDrawable.LINE);//直线

                break;
            case 3:
                gradientDrawable.setShape(GradientDrawable.RING);//圆环

                break;
        }
        gradientDrawable.setColor(colorFill);//设置填充色

        switch (gradientType) {
            case 0:
                gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//线性渐变
                //渐变方向,8个方向
                switch (orientationGradient) {
                    case 0:
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);//从上到下

                        break;
                    case 1:
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.TR_BL);//从右上到左下

                        break;
                    case 2:
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);//从右到左

                        break;
                    case 3:
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.BR_TL);//从右下到左上

                        break;
                    case 4:
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);//从下到上

                        break;
                    case 5:
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.BL_TR);//从左下到右上
                        break;
                    case 6:
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);//从左到右

                        break;
                    case 7:
                        gradientDrawable.setOrientation(GradientDrawable.Orientation.TL_BR);//从左上到右下

                        break;
                }
                break;
            case 1:
                gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);//辐射渐变
                gradientDrawable.setGradientRadius(radiusGradient);//设置渐变半径
                gradientDrawable.setGradientCenter(centerX, centerY);//设置渐变相对于控件的中心点坐标，如(0.3,0.6)
                break;
            case 2:
                gradientDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);//扫描渐变
                gradientDrawable.setGradientCenter(centerX, centerY);//设置渐变相对于控件的中心点坐标，如(0.3,0.6)

                break;
        }
        //设置渐变颜色
        if (colorStart != 0x00000000 && colorEnd != 0x00000000) {
            int[] colors = {colorStart, colorEnd};
            gradientDrawable.setColors(colors);
        }

        //描边
        gradientDrawable.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap);
        Drawable[] layers = {gradientDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        //设置描边方向，可控制每个方向的描边粗细和有无,不画线的方向，需要padding,strokeWidth的2倍
        layerDrawable.setLayerInset(0, strokePaddingLeft, strokePaddingTop, strokePaddingRight, strokePaddingBottom);
        /*
        设置drawable，大功告成
         */
        view.setBackground(layerDrawable);
        return this;
    }

    public GradientDrawable getGradientDrawable() {
        return gradientDrawable;
    }
}
