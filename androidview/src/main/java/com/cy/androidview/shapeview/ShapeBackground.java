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
    private int colorCenter = 0x00000000;
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
    }

    public int getRadiusCorner() {
        return radiusCorner;
    }

    /**
     * //圆角半径
     */
    public ShapeBackground setRadiusCorner(@StyleableRes int index) {
        this.radiusCorner = typedArray.getDimensionPixelSize(index, radiusCorner);
        return this;
    }
    public ShapeBackground setRadiusCorner_(int radiusCorner) {
        this.radiusCorner = radiusCorner;
        return this;
    }

    public int getRadiusTopLeft() {
        return radiusTopLeft;
    }

    /**
     * //左上角圆角半径
     */
    public ShapeBackground setRadiusTopLeft(@StyleableRes int index) {
        this.radiusTopLeft = typedArray.getDimensionPixelSize(index, radiusTopLeft);
        return this;

    }
    public ShapeBackground setRadiusTopLeft_(int radiusTopLeft) {
        this.radiusTopLeft = radiusTopLeft;
        return this;

    }

    public int getRadiusTopRight() {
        return radiusTopRight;
    }

    /**
     * //右上角圆角半径
     *
     * @param index
     */
    public ShapeBackground setRadiusTopRight(@StyleableRes int index) {
        this.radiusTopRight = typedArray.getDimensionPixelSize(index, radiusTopRight);
        return this;

    }
    public ShapeBackground setRadiusTopRight_(int radiusTopRight) {
        this.radiusTopRight = radiusTopRight;
        return this;

    }

    public int getRadiusBottomRight() {
        return radiusBottomRight;
    }

    /**
     * //右下角圆角半径
     *
     * @param index
     */
    public ShapeBackground setRadiusBottomRight(@StyleableRes int index) {
        this.radiusBottomRight = typedArray.getDimensionPixelSize(index, radiusBottomRight);
        return this;

    }
    public ShapeBackground setRadiusBottomRight_(int radiusBottomRight) {
        this.radiusBottomRight = radiusBottomRight;
        return this;

    }

    public int getRadiusBottomLeft() {
        return radiusBottomLeft;
    }

    /**
     * //左下角圆角半径
     *
     * @param index
     */
    public ShapeBackground setRadiusBottomLeft(@StyleableRes int index) {
        this.radiusBottomLeft = typedArray.getDimensionPixelSize(index, radiusBottomLeft);
        return this;

    }
    public ShapeBackground setRadiusBottomLeft_(int radiusBottomLeft ) {
        this.radiusBottomLeft = radiusBottomLeft;
        return this;

    }

    public int getColorFill() {
        return colorFill;
    }

    /**
     * //填充色
     *
     * @param index
     */
    public ShapeBackground setColorFill(@StyleableRes int index) {
        this.colorFill = typedArray.getColor(index, colorFill);
        return this;

    }
    public ShapeBackground setColorFill_(int colorFill) {
        this.colorFill = colorFill;
        return this;

    }

    public int getRadiusGradient() {
        return radiusGradient;
    }

    /**
     * //渐变相关
     * //渐变半径
     *
     * @param index
     */
    public ShapeBackground setRadiusGradient(@StyleableRes int index) {
        this.radiusGradient = typedArray.getDimensionPixelSize(index, radiusGradient);
        return this;

    }
    public ShapeBackground setRadiusGradient_(int radiusGradient) {
        this.radiusGradient = radiusGradient;
        return this;

    }

    public int getColorStart() {
        return colorStart;
    }

    /*
     //渐变开始颜色
     * @param index
     */
    public ShapeBackground setColorStart(@StyleableRes int index) {
        this.colorStart = typedArray.getColor(index, colorStart);
        return this;

    }
    public ShapeBackground setColorStart_(int colorStart) {
        this.colorStart =colorStart;
        return this;

    }

    public int getColorCenter() {
        return colorCenter;
    }

    /**
     * ///渐变中间颜色
     *
     * @param index
     */
    public ShapeBackground setColorCenter(@StyleableRes int index) {
        this.colorCenter = typedArray.getColor(index, colorCenter);
        return this;

    }
    public ShapeBackground setColorCenter_(int colorCenter) {
        this.colorCenter =colorCenter;
        return this;

    }

    public int getColorEnd() {
        return colorEnd;
    }

    /*
     //渐变结束颜色
     * @param index
     */
    public ShapeBackground setColorEnd(@StyleableRes int index) {
        this.colorEnd = typedArray.getColor(index, colorEnd);
        return this;

    }
    public ShapeBackground setColorEnd_(int colorEnd) {
        this.colorEnd = colorEnd;
        return this;

    }

    public int getOrientationGradient() {
        return orientationGradient;
    }

    /**
     * //渐变方向，默认从左到右
     *
     * @param index
     */
    public ShapeBackground setOrientationGradient(@StyleableRes int index) {
        this.orientationGradient = typedArray.getInt(index, orientationGradient);
        return this;

    }
    public ShapeBackground setOrientationGradient_(int orientationGradient) {
        this.orientationGradient = orientationGradient;
        return this;

    }

    public int getGradientType() {
        return gradientType;
    }

    /**
     * //渐变类型,默认线性渐变
     *
     * @param index
     */
    public ShapeBackground setGradientType(@StyleableRes int index) {
        this.gradientType = typedArray.getInt(index, gradientType);
        return this;

    }
    public ShapeBackground setGradientType_(int gradientType) {
        this.gradientType = gradientType;
        return this;

    }

    public float getCenterX() {
        return centerX;
    }

    /**
     * //渐变，相对于控件的中心点x坐标
     *
     * @param index
     */
    public ShapeBackground setCenterX(@StyleableRes int index) {
        this.centerX = typedArray.getFloat(index, centerX);
        return this;

    }
    public ShapeBackground setCenterX_(int centerX ) {
        this.centerX = centerX;
        return this;

    }

    public float getCenterY() {
        return centerY;
    }

    /**
     * //渐变，相对于控件的中心点y坐标
     *
     * @param index
     */
    public ShapeBackground setCenterY(@StyleableRes int index) {
        this.centerY = typedArray.getFloat(index, centerY);
        return this;

    }
    public ShapeBackground setCenterY_(int centerY) {
        this.centerY = centerY;
        return this;

    }

    public int getAngle() {
        return angle;
    }

    /**
     * //渐变方向,默认从左到右
     *
     * @param index
     */
    public ShapeBackground setAngle(@StyleableRes int index) {
        this.angle = typedArray.getInt(index, angle);
        return this;
    }
    public ShapeBackground setAngle_(int angle) {
        this.angle = angle;
        return this;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * //stroke，描边相关
     * //描边粗细，宽度
     *
     * @param index
     */
    public ShapeBackground setStrokeWidth(@StyleableRes int index) {
        this.strokeWidth = typedArray.getDimensionPixelSize(index, strokeWidth);
        return this;
    }
    public ShapeBackground setStrokeWidth_(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * //描边颜色
     *
     * @param index
     */
    public ShapeBackground setStrokeColor(@StyleableRes int index) {
        this.strokeColor = typedArray.getColor(index, strokeColor);
        return this;
    }
    public ShapeBackground setStrokeColor_(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public int getStrokeDashWidth() {
        return strokeDashWidth;
    }

    /**
     * //描边虚线宽度
     *
     * @param index
     */
    public ShapeBackground setStrokeDashWidth(@StyleableRes int index) {
        this.strokeDashWidth = typedArray.getDimensionPixelSize(index, strokeDashWidth);
        return this;
    }
    public ShapeBackground setStrokeDashWidth_(int strokeDashWidth) {
        this.strokeDashWidth =strokeDashWidth;
        return this;
    }

    public int getStrokeDashGap() {
        return strokeDashGap;
    }

    /**
     * //描边虚线间隔
     *
     * @param index
     */
    public ShapeBackground setStrokeDashGap(@StyleableRes int index) {
        this.strokeDashGap = typedArray.getDimensionPixelSize(index, strokeDashGap);
        return this;
    }
    public ShapeBackground setStrokeDashGap_(int strokeDashGap) {
        this.strokeDashGap = strokeDashGap;
        return this;
    }

    public int getStrokePaddingLeft() {
        return strokePaddingLeft;
    }

    /**
     * //描边左边padding,用于控制左边描边的粗细和有无
     *
     * @param index
     */
    public ShapeBackground setStrokePaddingLeft(@StyleableRes int index) {
        this.strokePaddingLeft = typedArray.getDimensionPixelSize(index, strokePaddingLeft);
        return this;
    }
    public ShapeBackground setStrokePaddingLeft_(int strokePaddingLeft) {
        this.strokePaddingLeft = strokePaddingLeft;
        return this;
    }

    public int getStrokePaddingTop() {
        return strokePaddingTop;
    }

    /**
     * //描边上边padding,用于控制上边描边的粗细和有无
     *
     * @param index
     */
    public ShapeBackground setStrokePaddingTop(@StyleableRes int index) {
        this.strokePaddingTop = typedArray.getDimensionPixelSize(index, strokePaddingTop);
        return this;
    }
    public ShapeBackground setStrokePaddingTop_(int strokePaddingTop) {
        this.strokePaddingTop =strokePaddingTop;
        return this;
    }

    public int getStrokePaddingRight() {
        return strokePaddingRight;
    }

    /**
     * //描边右边padding,用于控制右描边的粗细和有无
     *
     * @param index
     */
    public ShapeBackground setStrokePaddingRight(@StyleableRes int index) {
        this.strokePaddingRight = typedArray.getDimensionPixelSize(index, strokePaddingRight);
        return this;
    }
    public ShapeBackground setStrokePaddingRight_(int strokePaddingRight) {
        this.strokePaddingRight = strokePaddingRight;
        return this;
    }

    public int getStrokePaddingBottom() {
        return strokePaddingBottom;
    }

    /**
     * //描边下边padding,用于控制下描边的粗细和有无
     *
     * @param index
     */
    public ShapeBackground setStrokePaddingBottom(@StyleableRes int index) {
        this.strokePaddingBottom = typedArray.getDimensionPixelSize(index, strokePaddingBottom);
        return this;
    }
    public ShapeBackground setStrokePaddingBottom_(int strokePaddingBottom) {
        this.strokePaddingBottom = strokePaddingBottom;
        return this;
    }

    public int getShapeType() {
        return shapeType;
    }

    /**
     * //形状类型,默认矩形
     *
     * @param index
     */
    public ShapeBackground setShapeType(@StyleableRes int index) {
        this.shapeType = typedArray.getInt(index, shapeType);
        return this;
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
            int[] colors = {colorStart, colorCenter, colorEnd};
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
