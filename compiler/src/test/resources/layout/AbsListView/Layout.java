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
        final AbsListView absListView0 = obtainView(AbsListView.class);
        absListView0.setCacheColorHint(ContextCompat.getColor(context, R.color.colorPrimary));
        absListView0.setChoiceMode(2);
        absListView0.setDrawSelectorOnTop(true);
        absListView0.setFastScrollAlwaysVisible(true);
        absListView0.setFastScrollEnabled(true);
        absListView0.setFastScrollStyle(R.style.Widget_AppCompat_Button);
        absListView0.setScrollingCacheEnabled(true);
        absListView0.setSmoothScrollbarEnabled(true);
        absListView0.setStackFromBottom(true);
        absListView0.setTextFilterEnabled(true);
        absListView0.setTranscriptMode(2);
        ViewGroup.MarginLayoutParams lpAbsListView0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        absListView0.setLayoutParams(lpAbsListView0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(absListView0);
        }

        return absListView0;
    }
}