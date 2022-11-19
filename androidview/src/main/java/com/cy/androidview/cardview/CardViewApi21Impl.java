package com.cy.androidview.cardview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;

import androidx.annotation.Nullable;


public class CardViewApi21Impl implements CardViewImpl {
    CardViewApi21Impl() {
    }

    public void initialize(CardViewDelegate cardView, Context context, ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        RoundRectDrawable background = new RoundRectDrawable(backgroundColor, radius);
        cardView.setCardBackground(background);
        View view = cardView.getCardView();
        view.setClipToOutline(true);
        view.setElevation(elevation);
        this.setMaxElevation(cardView, maxElevation);
    }

    public void setRadius(CardViewDelegate cardView, float radius) {
        this.getCardBackground(cardView).setRadius(radius);
    }

    public void initStatic() {
    }

    public void setMaxElevation(CardViewDelegate cardView, float maxElevation) {
        this.getCardBackground(cardView).setPadding(maxElevation, cardView.getUseCompatPadding(), cardView.getPreventCornerOverlap());
        this.updatePadding(cardView);
    }

    public float getMaxElevation(CardViewDelegate cardView) {
        return this.getCardBackground(cardView).getPadding();
    }

    public float getMinWidth(CardViewDelegate cardView) {
        return this.getRadius(cardView) * 2.0F;
    }

    public float getMinHeight(CardViewDelegate cardView) {
        return this.getRadius(cardView) * 2.0F;
    }

    public float getRadius(CardViewDelegate cardView) {
        return this.getCardBackground(cardView).getRadius();
    }

    public void setElevation(CardViewDelegate cardView, float elevation) {
        cardView.getCardView().setElevation(elevation);
    }

    public float getElevation(CardViewDelegate cardView) {
        return cardView.getCardView().getElevation();
    }

    public void updatePadding(CardViewDelegate cardView) {

        if (!cardView.getUseCompatPadding()) {
            cardView.setShadowPadding(0, 0, 0, 0);
        } else {
            float elevation = this.getMaxElevation(cardView);
            float radius = this.getRadius(cardView);
            int hPadding = (int)Math.ceil((double)RoundRectDrawableWithShadow.calculateHorizontalPadding(elevation, radius, cardView.getPreventCornerOverlap()));
            int vPadding = (int)Math.ceil((double)RoundRectDrawableWithShadow.calculateVerticalPadding(elevation, radius, cardView.getPreventCornerOverlap()));
            cardView.setShadowPadding(hPadding, vPadding, hPadding, vPadding);
        }
    }

    public void onCompatPaddingChanged(CardViewDelegate cardView) {
        this.setMaxElevation(cardView, this.getMaxElevation(cardView));
    }

    public void onPreventCornerOverlapChanged(CardViewDelegate cardView) {
        this.setMaxElevation(cardView, this.getMaxElevation(cardView));
    }

    public void setBackgroundColor(CardViewDelegate cardView, @Nullable ColorStateList color) {
        this.getCardBackground(cardView).setColor(color);
    }

    public ColorStateList getBackgroundColor(CardViewDelegate cardView) {
        return this.getCardBackground(cardView).getColor();
    }

    private RoundRectDrawable getCardBackground(CardViewDelegate cardView) {
        return (RoundRectDrawable)cardView.getCardBackground();
    }
}

