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

        final View view0 = obtainView(View.class);
        view0.setBackground(new ColorDrawable(0xffff0000));
        view0.setId(R.id.view_0);
        RelativeLayout.LayoutParams lpView0 = new RelativeLayout.LayoutParams(dp(40, context), dp(40, context));
        lpView0.addRule(RelativeLayout.CENTER_IN_PARENT, true ? -1 : 0);
        view0.setLayoutParams(lpView0);
        relativeLayout0.addView(view0);

        final View view1 = obtainView(View.class);
        view1.setBackground(new ColorDrawable(0xff00ff00));
        view1.setId(R.id.view_1);
        RelativeLayout.LayoutParams lpView1 = new RelativeLayout.LayoutParams(dp(40, context), dp(40, context));
        lpView1.addRule(RelativeLayout.ALIGN_LEFT, R.id.view_0);
        lpView1.addRule(RelativeLayout.ALIGN_START, R.id.view_0);
        lpView1.addRule(RelativeLayout.ALIGN_TOP, R.id.view_0);
        lpView1.leftMargin = dp(66, context);
        lpView1.setMarginStart(dp(66, context));
        view1.setLayoutParams(lpView1);
        relativeLayout0.addView(view1);

        onFinishInflate();

        if (attachToRoot) {
            root.addView(relativeLayout0);
        }

        return relativeLayout0;
    }
}