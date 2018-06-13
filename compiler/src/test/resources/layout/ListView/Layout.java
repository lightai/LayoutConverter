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
        final ListView listView0 = obtainView(ListView.class);
        listView0.setDivider(new ColorDrawable(0xffffffff));
        listView0.setDividerHeight(dp(1, context));
        listView0.setFooterDividersEnabled(true);
        listView0.setHeaderDividersEnabled(true);
        listView0.setSelector(new ColorDrawable(0xffffffff));
        listView0.setOverscrollFooter(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background));
        listView0.setOverscrollHeader(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background));
        ViewGroup.MarginLayoutParams lpListView0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        listView0.setLayoutParams(lpListView0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(listView0);
        }

        return listView0;
    }
}