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
        final RelativeLayout relativeLayout0 = obtainView(RelativeLayout.class);
        ViewGroup.MarginLayoutParams lpRelativeLayout0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        relativeLayout0.setLayoutParams(lpRelativeLayout0);

        final TextView textView0 = obtainView(TextView.class);
        // Can't find attr: src:@drawable/icon
        textView0.setText("text");
        RelativeLayout.LayoutParams lpTextView0 = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        textView0.setLayoutParams(lpTextView0);
        relativeLayout0.addView(textView0);

        onFinishInflate();

        if (attachToRoot) {
            root.addView(relativeLayout0);
        }

        return relativeLayout0;
    }
}