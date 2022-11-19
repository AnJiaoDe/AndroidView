package com.cy.androidview.cardview;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.annotation.Nullable;

interface CardViewImpl {
    void initialize(CardViewDelegate var1, Context var2, ColorStateList var3, float var4, float var5, float var6);

    void setRadius(CardViewDelegate var1, float var2);

    float getRadius(CardViewDelegate var1);

    void setElevation(CardViewDelegate var1, float var2);

    float getElevation(CardViewDelegate var1);

    void initStatic();

    void setMaxElevation(CardViewDelegate var1, float var2);

    float getMaxElevation(CardViewDelegate var1);

    float getMinWidth(CardViewDelegate var1);

    float getMinHeight(CardViewDelegate var1);

    void updatePadding(CardViewDelegate var1);

    void onCompatPaddingChanged(CardViewDelegate var1);

    void onPreventCornerOverlapChanged(CardViewDelegate var1);

    void setBackgroundColor(CardViewDelegate var1, @Nullable ColorStateList var2);

    ColorStateList getBackgroundColor(CardViewDelegate var1);
}

