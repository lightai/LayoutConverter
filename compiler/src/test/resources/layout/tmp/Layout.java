package com.netease.layoutconverter.test;

import android.content.Context;
import android.view.*;
import android.widget.*;
import android.webkit.*;
import android.text.*;
import android.graphics.drawable.*;
import android.content.res.*;
import android.animation.*;
import com.netease.layoutconverter.BaseLayout;
import com.netease.layoutconverter.test.R;
import android.support.v4.content.ContextCompat;
import android.util.*;
import android.support.design.widget.*;

public class Layout extends BaseLayout {
    public static Layout from(Context context) {
        return new Layout(context);
    }

    private Layout(Context context) {
        super(context);
    }

    @Override public View inflate(ViewGroup root, boolean attachToRoot) {
        final TextView textView0 = obtainView(TextView.class);
        set_TextView_mTextSelectHandleRes(textView0, R.drawable.text_select_handle_middle);
        set_TextView_mTextSelectHandleLeftRes(textView0, R.drawable.text_select_handle_left);
        set_TextView_mTextSelectHandleRightRes(textView0, R.drawable.text_select_handle_right);
        ViewGroup.MarginLayoutParams lpTextView0 = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textView0.setLayoutParams(lpTextView0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(textView0);
        }

        return textView0;
    }
}