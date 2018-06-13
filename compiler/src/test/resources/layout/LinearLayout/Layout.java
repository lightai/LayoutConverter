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
        final LinearLayout linearLayout0 = obtainView(LinearLayout.class);
        linearLayout0.setGravity(Gravity.CENTER);
        linearLayout0.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        ViewGroup.MarginLayoutParams lpLinearLayout0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        linearLayout0.setLayoutParams(lpLinearLayout0);

        final View view0 = obtainView(View.class);
        LinearLayout.LayoutParams lpView0 = new LinearLayout.LayoutParams(dp(0, context), dp(40, context));
        lpView0.weight=1.0f;
        view0.setLayoutParams(lpView0);
        linearLayout0.addView(view0);

        onFinishInflate();

        if (attachToRoot) {
            root.addView(linearLayout0);
        }

        return linearLayout0;
    }
}