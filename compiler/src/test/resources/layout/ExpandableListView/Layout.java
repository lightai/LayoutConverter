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
        final ExpandableListView expandableListView0 = obtainView(ExpandableListView.class);
        expandableListView0.setChildDivider(ContextCompat.getDrawable(context, R.color.colorPrimary));
        expandableListView0.setChildIndicator(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));
        set_ExpandableListView_groupIndicator(expandableListView0, ContextCompat.getDrawable(context, R.drawable.ic_launcher_background));
        set_ExpandableListView_indicatorLeft(expandableListView0, dp(1, context));
        set_ExpandableListView_indicatorRight(expandableListView0, dp(1, context));
        ViewGroup.MarginLayoutParams lpExpandableListView0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        expandableListView0.setLayoutParams(lpExpandableListView0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(expandableListView0);
        }

        return expandableListView0;
    }
}