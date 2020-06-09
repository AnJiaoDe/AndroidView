package com.cy.androidview.shapeview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import com.cy.androidview.R;
import com.cy.androidview.rectangleview.RelativeLayoutRectangle;
import com.cy.androidview.rippleview.RelativeLayoutClick;


/**
 * Created by cy on 2018/10/24.
 */

public class RelativeLayoutRecShape extends RelativeLayoutClick {
    private float heightWidthRatio = 1; //高 / 宽（默认是高/宽），或者宽/高 比例
    private boolean baseOnWidthOrHeight = true;//默认true，即默认基于宽
    public RelativeLayoutRecShape(Context context) {
        this(context, null);
    }

    public RelativeLayoutRecShape(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayoutRecShape);
        heightWidthRatio = arr.getFloat(R.styleable.RelativeLayoutRecShape_heightWidthRatio, 1F);
        baseOnWidthOrHeight = arr.getBoolean(R.styleable.RelativeLayoutRecShape_baseOnWidthOrHeight, true);
        //水波纹的颜色,默认是0x66000000，建议自定义水波纹颜色的时候，用argb,rgb都设置为0，a可随意，调整透明度为了水波纹看起来更美观
        int colorRipple = arr.getColor(R.styleable.RelativeLayoutRecShape_colorRipple, 0x66000000);
        boolean havaRipple = arr.getBoolean(R.styleable.RelativeLayoutRecShape_haveRipple, true);//设置是否有水波纹点击效果，默认有

        int radiusCorner = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_radiusCorner, 0);//圆角半径
        int radiusTopLeft = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_radiusTopLeft, 0);//左上角圆角半径
        int radiusTopRight = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_radiusTopRight, 0);//右上角圆角半径
        int radiusBottomRight = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_radiusBottomRight, 0);//右下角圆角半径
        int radiusBottomLeft = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_radiusBottomLeft, 0);//左下角圆角半径
        int colorFill = arr.getColor(R.styleable.RelativeLayoutRecShape_colorFill, 0x00000000);//填充色

        //渐变相关
        int radiusGradient = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_radiusGradient, 0);//渐变半径
        int colorStart = arr.getColor(R.styleable.RelativeLayoutRecShape_colorStart, 0x00000000);//渐变开始颜色
        int colorCenter = arr.getColor(R.styleable.RelativeLayoutRecShape_colorCenter, 0x00000000);///渐变中间颜色
        int colorEnd = arr.getColor(R.styleable.RelativeLayoutRecShape_colorEnd, 0x00000000);//渐变结束颜色
        int orientationGradient = arr.getInt(R.styleable.RelativeLayoutRecShape_orientationGradient, 6);//渐变方向，默认从左到右
        int gradientType = arr.getInt(R.styleable.RelativeLayoutRecShape_gradientType, 0);//渐变类型,默认线性渐变
        float centerX = arr.getFloat(R.styleable.RelativeLayoutRecShape_centerX, 0.5f);//渐变，相对于控件的中心点x坐标
        float centerY = arr.getFloat(R.styleable.RelativeLayoutRecShape_centerY, 0.5f);//渐变，相对于控件的中心点y坐标
        int angle = arr.getInt(R.styleable.RelativeLayoutRecShape_angle, 0);//渐变方向,默认从左到右

        //stroke，描边相关
        int strokeWidth = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_strokeWidth, 0);//描边粗细，宽度
        int strokeColor = arr.getColor(R.styleable.RelativeLayoutRecShape_strokeColor, 0x00000000);//描边颜色
        int strokeDashWidth = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_strokeDashWidth, 0);//描边虚线宽度
        int strokeDashGap = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_strokeDashGap, 0);//描边虚线间隔
        //描边左边padding,用于控制左边描边的粗细和有无
        int strokePaddingLeft = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_strokePaddingLeft, 0);
        //描边上边padding,用于控制上边描边的粗细和有无
        int strokePaddingTop = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_strokePaddingTop, 0);
        //描边右边padding,用于控制右描边的粗细和有无
        int strokePaddingRight = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_strokePaddingRight, 0);
        //描边下边padding,用于控制下描边的粗细和有无
        int strokePaddingBottom = arr.getDimensionPixelSize(R.styleable.RelativeLayoutRecShape_strokePaddingBottom, 0);

        //形状类型,默认矩形
        int shapeType = arr.getInt(R.styleable.RelativeLayoutRecShape_shapeType, 0);
        arr.recycle();

//        //设置了填充色或者设置了渐变色的开始和结束，或者设置了描边颜色，才会设置drawable
        if (colorFill == 0x00000000 && strokeColor == 0x00000000 && (colorStart == 0x00000000 || colorEnd == 0x00000000)) return;

        GradientDrawable gradientDrawable = new GradientDrawable();//创建背景drawable
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
        //5.0以上才有效,
        if (havaRipple&&android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            //当控件设置了点击监听器，并且控件点击有效，时，才能产生水波纹
            RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(colorRipple), layerDrawable, null);

            setBackground(rippleDrawable);

            return;
        }

        setBackground(layerDrawable);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //默认基于宽，即高会和宽度一致，高由宽决定
        if (baseOnWidthOrHeight) {
            int widthSize = getMeasuredWidth();
            setMeasuredDimension(widthSize,(int) (widthSize * heightWidthRatio));
        } else {
            //基于高，即宽度会和高度一致，宽度由高度决定
            int heightSize = getMeasuredHeight();
            setMeasuredDimension((int) (heightSize * heightWidthRatio),heightSize);
        }
    }
}
