package com.cy.androidview.edittext;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by lenovo on 2017/8/2.
 */

public abstract class TextWatcherImpl implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public abstract void afterTextChanged(Editable s) ;
}