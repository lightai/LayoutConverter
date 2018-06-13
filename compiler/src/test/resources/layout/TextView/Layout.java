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
        textView0.setAutoLinkMask(1);
        textView0.setContentDescription("desc");
        setTextViewDrawableLeft(textView0, ContextCompat.getDrawable(context, android.R.drawable.arrow_down_float));
        textView0.setCompoundDrawablePadding(dp(12, context));
        textView0.setDuplicateParentStateEnabled(true);
        textView0.setElegantTextHeight(true);
        textView0.setHint("hint");
        textView0.setImeOptions(2);
        textView0.setRawInputType(1);
        textView0.setLines(3);
        textView0.setMaxLines(3);
        textView0.setText("text");
        textView0.setTextAppearance(R.style.TextAppearance_AppCompat);
        textView0.setTextColor(ContextCompat.getColorStateList(context, android.R.color.background_light));
        textView0.setHighlightColor(ContextCompat.getColor(context, android.R.color.background_light));
        textView0.setHintTextColor(ContextCompat.getColorStateList(context, android.R.color.holo_blue_bright));
        textView0.setLinkTextColor(ContextCompat.getColorStateList(context, android.R.color.holo_blue_bright));
        set_TextView_mCursorDrawableRes(textView0, android.R.drawable.alert_dark_frame);
        textView0.setTextDirection(0);
        textView0.setTextIsSelectable(true);
        textView0.setTextScaleX(0.8f);
        textView0.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp(10, context));
        setTextViewStyle(textView0, 1);
        setTextViewTypeface(textView0, 1);
        ViewGroup.MarginLayoutParams lpTextView0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        textView0.setLayoutParams(lpTextView0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(textView0);
        }

        return textView0;
    }
}