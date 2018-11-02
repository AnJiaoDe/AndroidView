package com.cy.necessaryview.edittext;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by lenovo on 2017/8/2.
 */

public abstract   class TextWatcherImpl implements TextWatcher {

    public  void cyBeforeTextChanged( CharSequence s, int start, int count, int after){}

    public  void cyOnTextChanged( CharSequence s, int start, int before, int count) {}

    public abstract void cyAfterTextChanged( Editable s);

    @Override

    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
        cyBeforeTextChanged(s, start, count, after);
    }

    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {
        cyOnTextChanged( s, start, before, count);
    }

    @Override
    public final void afterTextChanged(Editable s) {
        cyAfterTextChanged( s);
    }
}