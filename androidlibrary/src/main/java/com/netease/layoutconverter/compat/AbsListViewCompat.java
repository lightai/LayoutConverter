package com.netease.layoutconverter.compat;

import android.os.Build;
import android.widget.AbsListView;

/**
 * Created by hzwangpeng2015 on 2018/2/27.
 */
public class AbsListViewCompat {
    public static void setFastScrollStyle(AbsListView view, int styleResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setFastScrollStyle(styleResId);
        }
    }
}
