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
        final View view0 = obtainView(View.class);
        setViewPaddingBottom(view0, in(1, context));
        setViewPaddingRight(view0, sp(1, context));
        setViewPaddingTop(view0, 1);
        ViewGroup.MarginLayoutParams lpView0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        lpView0.bottomMargin = (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
        lpView0.leftMargin = dp(1, context);
        lpView0.rightMargin = mm(1, context);
        lpView0.topMargin = pt(1, context);
        view0.setLayoutParams(lpView0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(view0);
        }

        return view0;
    }
}