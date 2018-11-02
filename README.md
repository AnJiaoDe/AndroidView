[简书_https://www.jianshu.com/p/fa5f5cb3edd1](https://www.jianshu.com/p/fa5f5cb3edd1)

[APKdemo_https://github.com/AnJiaoDe/AndroidNecessaryView/blob/master/app/build/outputs/apk/app-debug.apk](https://github.com/AnJiaoDe/AndroidNecessaryView/blob/master/app/build/outputs/apk/app-debug.apk)

@[toc]


**使用方法：将libray模块复制到项目中,或者直接在build.gradle中依赖:**

```
allprojects {
		repositories {
			
			maven { url 'https://jitpack.io' }
		}
	}
```

```
dependencies {
	                  compile 'com.github.AnJiaoDe:AndroidNecessaryView:V1.0.1'



	}
```
**注意：如果sync报错，是因为和com.android.tools.build:gradle 3.0有关，**
**可以改将compile改为implementation 或者api** 
**注意：水波纹效果，5.0及以上才有效,需要设置点击事件监听器，才能看到**



## 1.控件点击效果（水波纹、图片滤镜）免去Selector和一个按钮2张图片的麻烦

![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-884198e435d908c4.gif?imageMogr2/auto-orient/strip)
**ClickImageView点击效果有滤镜和波纹2种，默认是滤镜，你可以设置为波纹效果**
使用方法：

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_click"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#eee"
    android:orientation="vertical"
    android:padding="10dp">

    <!--默认水波纹-->

    <com.cy.necessaryview.rippleview.ClickFrameLayout
        android:id="@+id/rpfl"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:background="#fff" />

    <!--自定义水波纹颜色-->
    <com.cy.necessaryview.rippleview.ClickFrameLayout
        android:id="@+id/rpfl2"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:layout_marginTop="10dp"
        android:background="#99ff0000"

        app:colorRipple="#fff" />

    <!--设置imageview水波纹，默认是超过边界-->
    <com.cy.necessaryview.rippleview.ClickImageView
        android:id="@+id/civ1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:padding="10dp"
        android:scaleType="centerInside"
        android:src="@drawable/after_sale_icon"
        app:haveRipple="true" />

    <!--设置imageview水波纹，设置不超过边界-->
    <com.cy.necessaryview.rippleview.ClickImageView
        android:id="@+id/civ2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:padding="10dp"
        android:scaleType="centerInside"
        android:src="@drawable/after_sale_icon"
        app:haveRipple="true"
        app:rippleOver="false" />
    <!--imageview,点击效果默认是滤镜-->
    <com.cy.necessaryview.rippleview.ClickImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:padding="10dp"
        android:scaleType="centerInside"
        android:src="@drawable/after_sale_icon" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <!--imageview,默认滤镜效果-->
        <com.cy.necessaryview.rippleview.ClickImageView
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginTop="10dp"
            android:scaleType="centerCrop"
            android:src="@drawable/hg" />

        <!--imageview，自定义亮度-->
        <com.cy.necessaryview.rippleview.ClickImageView
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginLeft="10dp"
            android:layout_marginTop="10dp"
            android:scaleType="centerCrop"
            android:src="@drawable/hg"
            app:filterLightNumber="100" />

        <!--imageview，这是点击变亮-->
        <com.cy.necessaryview.rippleview.ClickImageView
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginLeft="10dp"
            android:layout_marginTop="10dp"

            android:scaleType="centerCrop"
            android:src="@drawable/hg"
            app:filterLightOrDark="true" />
    </LinearLayout>

</LinearLayout>

```

## 2.比例控件（默认是正方形，还可以自定义比例,继承自水波纹控件，默认有点击效果）
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-584fff7d46574340.gif?imageMogr2/auto-orient/strip)

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_rectangle"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="10dp">
    <com.cy.necessaryview.rectangleview.RectangleImageView
        android:id="@+id/riv"
        android:layout_width="50dp"
        android:layout_height="wrap_content"
        android:scaleType="centerCrop"
        android:src="@drawable/hg" />

    <com.cy.necessaryview.rectangleview.RectangleLinearLayout

        android:layout_width="100dp"
        android:layout_height="100000dp"
        android:layout_marginTop="10dp"
        android:background="#f00">

    </com.cy.necessaryview.rectangleview.RectangleLinearLayout>

    <com.cy.necessaryview.rectangleview.RectangleRelativeLayout

        android:layout_width="50dp"
        android:layout_height="100000dp"
        android:layout_marginTop="10dp"
        android:background="#f78900"
        app:heightWidthRatio="1.7">

    </com.cy.necessaryview.rectangleview.RectangleRelativeLayout>


    <com.cy.necessaryview.rectangleview.RectangleLinearLayout

        android:layout_width="wrap_content"
        android:layout_height="10000dp"
        android:layout_marginTop="10dp"

        android:background="#ff0">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="即热晶体管" />
    </com.cy.necessaryview.rectangleview.RectangleLinearLayout>

    <!--上面全是以宽为基准，下面全是以高为基准-->
    <com.cy.necessaryview.rectangleview.RectangleFrameLayout

        android:id="@+id/rfl"
        android:layout_width="103330dp"
        android:layout_height="50dp"
        android:layout_marginTop="10dp"

        android:background="#ff6990"
        app:baseOnWidthOrHeight="false">

    </com.cy.necessaryview.rectangleview.RectangleFrameLayout>

    <com.cy.necessaryview.rectangleview.RectangleLinearLayout

        android:layout_width="10000dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"

        android:background="#00789f"

        app:baseOnWidthOrHeight="false">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="即热晶\n体管\n近日刚进\n入提\n货价h\nit\n\n好几天假hi" />
    </com.cy.necessaryview.rectangleview.RectangleLinearLayout>
</LinearLayout>

```

## 3.各种shape背景（圆角，画线，虚线、渐变、边框、圆形、椭圆,继承自水波纹控件，默认有点击效果）
![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-3a64ca146991899a.gif?imageMogr2/auto-orient/strip)

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_round"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#eee"
    android:orientation="horizontal"
    android:padding="5dp">

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <!--圆角+填充色-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="100dp"
            android:id="@+id/rsf"
            android:layout_height="40dp"
            app:colorFill="#fff"
            app:radiusCorner="18dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--设置了background,没有设置填充色，圆角失效-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="20dp"
            android:layout_height="20dp"
            android:layout_marginTop="5dp"
            android:background="#678798"
            app:radiusCorner="5dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>


        <!--设置填充色，圆角才生效-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            android:background="#f00"

            app:colorFill="#956890"
            app:radiusCorner="5dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--四个角的弧度-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:colorFill="#456440"
            app:radiusBottomLeft="40dp"
            app:radiusBottomRight="30dp"

            app:radiusTopLeft="10dp"
            app:radiusTopRight="20dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--设置了4个圆角弧度，设置了radiusCorner，radiusCorner优先级更高-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:colorFill="#943440"
            app:radiusBottomLeft="40dp"
            app:radiusBottomRight="30dp"

            app:radiusCorner="5dp"
            app:radiusTopLeft="10dp"
            app:radiusTopRight="20dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--线性渐变,默认从左到右-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="100dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:colorCenter="#aaFF3C4D"
            app:colorEnd="#ff0000"
            app:colorStart="#33FF7580"
            app:radiusBottomLeft="40dp"
            app:radiusBottomRight="30dp"

            app:radiusCorner="10dp"
            app:radiusTopLeft="10dp"
            app:radiusTopRight="20dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--设置某个角有弧度,线性渐变，TR_BL从右上角到左下角-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:colorCenter="#aaFF3C4D"
            app:colorEnd="#ff0000"
            app:colorStart="#33FF7580"
            app:orientationGradient="tr_bl"
            app:radiusTopLeft="10dp"
            app:radiusTopRight="10dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--扫描渐变，中心点默认控件中心位置-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:colorCenter="#aaFF3C4D"
            app:colorEnd="#ff0000"
            app:colorStart="#33FF7580"
            app:gradientType="sweep_gradient"
            app:radiusBottomLeft="40dp"
            app:radiusBottomRight="30dp"
            app:radiusCorner="5dp"
            app:radiusTopLeft="10dp"
            app:radiusTopRight="20dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--扫描渐变，中心点在控件水平30%位置，控件竖直30%位置-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:centerX="0.3"
            app:centerY="0.3"
            app:colorCenter="#aaFF3C4D"
            app:colorEnd="#ff0000"
            app:colorStart="#33FF7580"
            app:gradientType="sweep_gradient"

            app:radiusBottomLeft="40dp"
            app:radiusBottomRight="30dp"
            app:radiusCorner="5dp"
            app:radiusTopLeft="10dp"
            app:radiusTopRight="20dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--辐射渐变，，中心点默认控件中心位置-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="150dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:colorCenter="#77ff0000"
            app:colorEnd="#66ff0000"
            app:colorStart="#88ff0000"
            app:gradientType="radial_gradient"
            app:radiusCorner="50dp"
            app:radiusGradient="20dp"></com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--辐射渐变，中心点在控件水平30%位置，控件竖直30%位置-->

        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="150dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:centerX="0.3"

            app:centerY="0.3"
            app:colorCenter="#99ff0000"
            app:colorEnd="#66ff0000"
            app:colorStart="#ff0000"
            app:gradientType="radial_gradient"
            app:radiusCorner="50dp"
            app:radiusGradient="20dp"></com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--辐射渐变，中心点在控件水平70%位置，控件竖直50%位置-->

        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="150dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:centerX="0.7"

            app:centerY="0.5"
            app:colorCenter="#99ff0000"
            app:colorEnd="#66ff0000"
            app:colorStart="#ff0000"
            app:gradientType="radial_gradient"
            app:radiusCorner="50dp"
            app:radiusGradient="10dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

    </LinearLayout>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_marginLeft="20dp"
        android:orientation="vertical">

        <!--描边-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="100dp"
            android:layout_height="40dp"

            app:colorFill="#f00"
            app:radiusCorner="5dp"
            app:strokeColor="#FF979797"
            app:strokeWidth="2dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>
        <!--描边-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="100dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:radiusCorner="5dp"
            app:strokeColor="#FF979797"

            app:strokeWidth="1dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--虚线框,strokeDashWidth:虚线长度，strokeDashGap：虚线间隔-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:layout_marginTop="5dp"
            app:radiusCorner="4dp"
            app:strokeColor="#FF979797"
            app:strokeDashGap="4dp"
            app:strokeDashWidth="4dp"
            app:strokeWidth="2dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>


        <!--椭圆-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="60dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:colorFill="#f00"
            app:shapeType="oval">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>

        <!--椭圆，辐射渐变,描边-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="60dp"
            android:layout_height="40dp"
            android:layout_marginTop="5dp"
            app:colorCenter="#99ff0000"
            app:colorEnd="#66ff0000"
            app:colorStart="#ff0000"
            app:gradientType="radial_gradient"
            app:radiusGradient="20dp"
            app:shapeType="oval"
            app:strokeColor="#88454545"
            app:strokeWidth="2dp">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>
        <!--圆形,辐射渐变,描边-->
        <com.cy.necessaryview.shapeview.RecShapeFrameLayout
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:layout_marginTop="5dp"
            app:colorCenter="#aaFF3C4D"
            app:colorEnd="#ff0000"
            app:colorStart="#33FF7580"
            app:gradientType="sweep_gradient"
            app:radiusGradient="20dp"
            app:shapeType="oval">

        </com.cy.necessaryview.shapeview.RecShapeFrameLayout>


        <!--控件下画线,不画线的方向，需要padding,strokeWidth的2倍-->
        <com.cy.necessaryview.shapeview.RecShapeTextView
            android:id="@+id/recShapeTextView"
            android:layout_width="150dp"
            android:layout_height="40dp"

            android:layout_marginTop="15dp"
            android:gravity="center"
            android:text="控件下画线"
            android:textSize="14sp"
            app:strokeColor="#454545"
            app:strokePaddingLeft="-2dp"
            app:strokePaddingRight="-2dp"
            app:strokePaddingTop="-2dp"
            app:strokeWidth="1dp" />
        <!--控件右画线,不画线的方向，需要padding,strokeWidth的2倍-->
        <com.cy.necessaryview.shapeview.RecShapeTextView
            android:layout_width="150dp"
            android:layout_height="40dp"
            android:layout_marginTop="15dp"

            android:gravity="center"
            android:text="控件右画线"
            android:textSize="14sp"
            app:strokeColor="#454545"
            app:strokePaddingBottom="-2dp"
            app:strokePaddingLeft="-2dp"
            app:strokePaddingTop="-2dp"
            app:strokeWidth="1dp" />
        <!--控件画线,画线粗细可以不一致-->

        <com.cy.necessaryview.shapeview.RecShapeTextView
            android:layout_width="150dp"
            android:layout_height="40dp"
            android:layout_marginTop="15dp"

            android:gravity="center"
            android:text="画线粗细可以不一致"
            android:textSize="10sp"
            app:strokeColor="#454545"
            app:strokePaddingBottom="-2dp"
            app:strokePaddingLeft="-4dp"
            app:strokePaddingTop="-4dp"
            app:strokeWidth="2dp" />

        <!--使用比例，控制为正方形,圆角+填充色-->
        <com.cy.necessaryview.shapeview.RecShapeTextView
            android:layout_width="90dp"
            android:layout_height="40dp"
            android:layout_marginTop="10dp"
            android:gravity="center"
            android:padding="2dp"
            android:text="RecShapeTextView\n带shape的正方形textView"
            android:textColor="#fff"
            android:textSize="10sp"
            app:colorFill="#f00"
            app:radiusCorner="18dp"
            app:heightWidthRatio="1" />
    </LinearLayout>
</LinearLayout>

```

## 4.圆角ImageView（可加边框）
引用自[https://github.com/vinc3m1/RoundedImageView](https://github.com/vinc3m1/RoundedImageView)

![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-b8f9f0a39e8b92b4?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_rounded_iv"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="10dp">

    <com.makeramen.roundedimageview.RoundedImageView
        android:layout_width="70dp"
        android:layout_height="70dp"

        android:layout_marginTop="10dp"
        android:scaleType="centerCrop"
        android:src="@drawable/hg"
        app:riv_corner_radius="10dp" />

    <com.makeramen.roundedimageview.RoundedImageView
        android:layout_width="70dp"
        android:layout_height="70dp"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        android:scaleType="centerCrop"


        android:src="@drawable/hg"

        app:riv_oval="true" />

    <com.makeramen.roundedimageview.RoundedImageView
        android:layout_width="100dp"
        android:layout_height="70dp"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        android:scaleType="centerCrop"

        android:src="@drawable/hg"

        app:riv_border_width="2dp"
        app:riv_border_color="#f00"

        app:riv_oval="true" />
</LinearLayout>

```

## 5.Selector（CheckBox,RadioButton,ImageView）设置button选中、未选中的drawable,设置bg选中未选中，设置src选中未选中

![在这里插入图片描述](http://upload-images.jianshu.io/upload_images/11866078-e63708687ab46422.gif?imageMogr2/auto-orient/strip)

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_click"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#eee"
    android:orientation="vertical"
    android:padding="10dp">

    <com.cy.necessaryview.selectorview.SelectorCheckBox
        android:layout_width="wrap_content"
        android:layout_height="40dp"
        android:gravity="center_vertical"
        android:paddingLeft="5dp"
        android:text="selectoreCheckBox"
        android:textSize="16sp"
        app:buttonChecked="@drawable/cb_zuji_selected"
        app:buttonUnChecked="@drawable/cb_zuji_normal" />

    <com.cy.necessaryview.selectorview.SelectorCheckBox
        android:layout_width="wrap_content"
        android:layout_height="40dp"
        android:gravity="center_vertical"
        android:paddingLeft="5dp"
        android:text="selectoreCheckBox"
        android:textSize="16sp"
        app:buttonChecked="@drawable/cb_zuji_selected"
        app:buttonUnChecked="@drawable/cb_zuji_normal"
        app:textColorChecked="#ff0000"
        app:textColorUnChecked="#454545" />

    <com.cy.necessaryview.selectorview.SelectorCheckBox
        android:layout_width="wrap_content"
        android:layout_height="40dp"
        android:gravity="center_vertical"
        android:paddingLeft="5dp"
        android:text="selectoreCheckBox"
        android:textSize="16sp"
        app:backgroundChecked="#33ff0000"
        app:backgroundUnChecked="#fff"
        app:buttonChecked="@drawable/cb_zuji_selected"
        app:buttonUnChecked="@drawable/cb_zuji_normal"
        app:textColorChecked="#ff0000"
        app:textColorUnChecked="#454545" />

    <com.cy.necessaryview.selectorview.SelectorCheckBox
        android:layout_width="wrap_content"
        android:layout_height="40dp"
        android:layout_marginTop="10dp"
        android:button="@null"
        android:gravity="center_vertical"
        android:paddingLeft="5dp"
        android:text="selectoreCheckBox"
        android:textSize="16sp"
        app:backgroundChecked="#33ff0000"
        app:backgroundUnChecked="#fff"
        app:textColorChecked="#ff0000"
        app:textColorUnChecked="#454545" />

    <RadioGroup
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:layout_marginTop="10dp"
        android:orientation="horizontal">

        <com.cy.necessaryview.selectorview.SelectorRadioButton
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:gravity="center_vertical"
            android:text="SelectorRadioButton"
            android:textSize="14sp"
            app:buttonChecked="@drawable/cb_zuji_selected"
            app:buttonUnChecked="@drawable/cb_zuji_normal" />

        <com.cy.necessaryview.selectorview.SelectorRadioButton
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:gravity="center_vertical"

            android:text="SelectorRadioButton"
            android:textSize="14sp"
            app:buttonChecked="@drawable/cb_zuji_selected"
            app:buttonUnChecked="@drawable/cb_zuji_normal" />
    </RadioGroup>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="SelectorImageView"/>
    <com.cy.necessaryview.selectorview.SelectorImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="10dp"
        app:srcChecked="@drawable/cb_zuji_selected"
        app:srcUnChecked="@drawable/cb_zuji_normal" />

    <com.cy.necessaryview.selectorview.SelectorImageView
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_marginTop="10dp"
        app:bgChecked="@drawable/hg"
        app:bgUnChecked="#f00" />

</LinearLayout>

```

## 源码：

```
public class ClickFrameLayout extends FrameLayout {
    public ClickFrameLayout(Context context) {
        this(context, null);
    }

    public ClickFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ClickFrameLayout);
        //水波纹的颜色,默认是0x66000000，建议自定义水波纹颜色的时候，用argb,rgb都设置为0，a可随意，调整透明度为了水波纹看起来更美观
        int colorRipple = arr.getColor(R.styleable.ClickFrameLayout_colorRipple, 0x66000000);
        boolean havaRipple = arr.getBoolean(R.styleable.ClickFrameLayout_haveRipple, true);//设置是否有水波纹点击效果，默认有
        arr.recycle();
        //5.0以上才有效,
        if (havaRipple&&android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = getBackground();

            //当控件设置了点击监听器，并且控件点击有效，时，才能产生水波纹
            RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(colorRipple), drawable, null);

            setBackground(rippleDrawable);
        }

    }
}

```

```
public class RectangleFrameLayout extends ClickFrameLayout {
    private float heightWidthRatio = 1; //高 / 宽（默认是高/宽），或者宽/高 比例
    private boolean baseOnWidthOrHeight = true;//默认true，即默认基于宽

    public RectangleFrameLayout(Context context) {
        this(context, null);
    }

    public RectangleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.RectangleFrameLayout);
        heightWidthRatio = arr.getFloat(R.styleable.RectangleFrameLayout_heightWidthRatio, 1F);
        baseOnWidthOrHeight = arr.getBoolean(R.styleable.RectangleFrameLayout_baseOnWidthOrHeight, true);
        arr.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //默认基于宽，即高会和宽度一致，高由宽决定
        if (baseOnWidthOrHeight) {

            int childWidthSize = getMeasuredWidth();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * heightWidthRatio), MeasureSpec.EXACTLY);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        } else {

            //基于高，即宽度会和高度一致，宽度由高度决定
            int childHeightSize = getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childHeightSize * heightWidthRatio), MeasureSpec.EXACTLY);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        }
    }


}

```

```
public class SelectorCheckBox extends AppCompatCheckBox {
    private int backgroundID, backgroundCheckedID, bg_color, bg_checked_color,
            textColorID, textColorCheckedID,
            buttonRes, buttonCheckedRes;
    private SelectorOnCheckedChangeListener selectorOnCheckedChangeListener;

    private boolean myListener = true;

    public SelectorCheckBox(Context context) {
        this(context, null);
    }

    public SelectorCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.SelectorCheckBox);

        backgroundID = arr.getResourceId(R.styleable.SelectorCheckBox_backgroundUnChecked, -1);//未选中的背景资源
        backgroundCheckedID = arr.getResourceId(R.styleable.SelectorCheckBox_backgroundChecked, -1);//选中的背景资源

        if (backgroundID == -1) {
            bg_color = arr.getColor(R.styleable.SelectorCheckBox_backgroundUnChecked, 0x00000000);//未选中的背景颜色

        }
        if (backgroundCheckedID == -1) {
            bg_checked_color = arr.getColor(R.styleable.SelectorCheckBox_backgroundChecked, 0x00000000);//选中的背景颜色

        }

        buttonRes = arr.getResourceId(R.styleable.SelectorCheckBox_buttonUnChecked, -1);//未选中的按钮资源
        buttonCheckedRes = arr.getResourceId(R.styleable.SelectorCheckBox_buttonChecked, -1);//选中的按钮资源

        textColorID = arr.getColor(R.styleable.SelectorCheckBox_textColorUnChecked, getCurrentTextColor());//未选中的文字颜色
        textColorCheckedID = arr.getColor(R.styleable.SelectorCheckBox_textColorChecked, getCurrentTextColor());//选中的文字颜色
        arr.recycle();

        if (isChecked()) {

            setResOnChecked();

        } else {
            setResOnUnChecked();

        }

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setResOnChecked();

                } else {
                    setResOnUnChecked();


                }
                if (selectorOnCheckedChangeListener != null) {
                    selectorOnCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            }
        });

    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        if (myListener) {

            super.setOnCheckedChangeListener(listener);
            myListener = false;
        }
    }

    //监听器使用这个方法
    public void setSelectorOnCheckedChangeListener(SelectorOnCheckedChangeListener listener) {
        this.selectorOnCheckedChangeListener = listener;
    }

    public interface SelectorOnCheckedChangeListener extends OnCheckedChangeListener {
        @Override
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    }

    //设置选中时的背景，文字颜色等
    private void setResOnChecked() {
        if (backgroundCheckedID != -1) {

            setBackgroundResource(backgroundCheckedID);
        } else {
            setBackgroundColor(bg_checked_color);
        }
        if (buttonCheckedRes != -1) {
            setButtonDrawable(buttonCheckedRes);
        }

        setTextColor(textColorCheckedID);
    }
    //设置未选中时的背景，文字颜色等

    private void setResOnUnChecked() {
        if (backgroundID != -1) {

            setBackgroundResource(backgroundID);
        } else {
            setBackgroundColor(bg_color);
        }
        if (buttonRes != -1) {
            setButtonDrawable(buttonRes);
        }


        setTextColor(textColorID);
    }
}

```

```
public class RecShapeFrameLayout extends FrameLayout {
    private float heightWidthRatio = 0; //高 / 宽（默认是高/宽），或者宽/高 比例
    private boolean baseOnWidthOrHeight = true;//默认true，即默认基于宽

    public RecShapeFrameLayout(Context context) {
        this(context, null);
    }


    public RecShapeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.RecShapeFrameLayout);


        heightWidthRatio = arr.getFloat(R.styleable.RecShapeFrameLayout_heightWidthRatio, 0F);
        baseOnWidthOrHeight = arr.getBoolean(R.styleable.RecShapeFrameLayout_baseOnWidthOrHeight, true);

        //水波纹的颜色,默认是0x66000000，建议自定义水波纹颜色的时候，用argb,rgb都设置为0，a可随意，调整透明度为了水波纹看起来更美观
        int colorRipple = arr.getColor(R.styleable.RecShapeFrameLayout_colorRipple, 0x66000000);
        boolean havaRipple = arr.getBoolean(R.styleable.RecShapeFrameLayout_haveRipple, true);//设置是否有水波纹点击效果，默认有

        int radiusCorner = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_radiusCorner, 0);//圆角半径
        int radiusTopLeft = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_radiusTopLeft, 0);//左上角圆角半径
        int radiusTopRight = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_radiusTopRight, 0);//右上角圆角半径
        int radiusBottomRight = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_radiusBottomRight, 0);//右下角圆角半径
        int radiusBottomLeft = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_radiusBottomLeft, 0);//左下角圆角半径
        int colorFill = arr.getColor(R.styleable.RecShapeFrameLayout_colorFill, 0x00000000);//填充色

        //渐变相关
        int radiusGradient = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_radiusGradient, 0);//渐变半径
        int colorStart = arr.getColor(R.styleable.RecShapeFrameLayout_colorStart, 0x00000000);//渐变开始颜色
        int colorCenter = arr.getColor(R.styleable.RecShapeFrameLayout_colorCenter, 0x00000000);///渐变中间颜色
        int colorEnd = arr.getColor(R.styleable.RecShapeFrameLayout_colorEnd, 0x00000000);//渐变结束颜色
        int orientationGradient = arr.getInt(R.styleable.RecShapeFrameLayout_orientationGradient, 6);//渐变方向，默认从左到右
        int gradientType = arr.getInt(R.styleable.RecShapeFrameLayout_gradientType, 0);//渐变类型,默认线性渐变
        float centerX = arr.getFloat(R.styleable.RecShapeFrameLayout_centerX, 0.5f);//渐变，相对于控件的中心点x坐标
        float centerY = arr.getFloat(R.styleable.RecShapeFrameLayout_centerY, 0.5f);//渐变，相对于控件的中心点y坐标
        int angle = arr.getInt(R.styleable.RecShapeFrameLayout_angle, 0);//渐变方向,默认从左到右

        //stroke，描边相关
        int strokeWidth = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_strokeWidth, 0);//描边粗细，宽度
        int strokeColor = arr.getColor(R.styleable.RecShapeFrameLayout_strokeColor, 0x00000000);//描边颜色
        int strokeDashWidth = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_strokeDashWidth, 0);//描边虚线宽度
        int strokeDashGap = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_strokeDashGap, 0);//描边虚线间隔
        //描边左边padding,用于控制左边描边的粗细和有无
        int strokePaddingLeft = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_strokePaddingLeft, 0);
        //描边上边padding,用于控制上边描边的粗细和有无
        int strokePaddingTop = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_strokePaddingTop, 0);
        //描边右边padding,用于控制右描边的粗细和有无
        int strokePaddingRight = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_strokePaddingRight, 0);
        //描边下边padding,用于控制下描边的粗细和有无
        int strokePaddingBottom = arr.getDimensionPixelSize(R.styleable.RecShapeFrameLayout_strokePaddingBottom, 0);

        //形状类型,默认矩形
        int shapeType = arr.getInt(R.styleable.RecShapeFrameLayout_shapeType, 0);
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

        if (heightWidthRatio == 0) return;
        //默认基于宽，即高会和宽度一致，高由宽决定
        if (baseOnWidthOrHeight) {

            int childWidthSize = getMeasuredWidth();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * heightWidthRatio), MeasureSpec.EXACTLY);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        } else {

            //基于高，即宽度会和高度一致，宽度由高度决定
            int childHeightSize = getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childHeightSize * heightWidthRatio), MeasureSpec.EXACTLY);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        }
    }
}

```
## 各位老铁有问题欢迎及时联系、指正、批评、撕逼

[GitHub_https://github.com/AnJiaoDe](https://github.com/AnJiaoDe)

关注专题[Android开发常用开源库_https://www.jianshu.com/c/3ff4b3951dc5](https://www.jianshu.com/c/3ff4b3951dc5)

[简书_https://www.jianshu.com/u/b8159d455c69](https://www.jianshu.com/u/b8159d455c69)

 微信公众号
 ![这里写图片描述](http://upload-images.jianshu.io/upload_images/11866078-fcfbb45175f99de0?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

QQ群
![这里写图片描述](http://upload-images.jianshu.io/upload_images/11866078-a31ff40ac6850a6d?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
