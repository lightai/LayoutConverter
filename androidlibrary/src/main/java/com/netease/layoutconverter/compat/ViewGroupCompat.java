package com.netease.layoutconverter.compat;

import android.os.Build;
import android.view.ViewGroup;

/**
 * Created by hzwangpeng2015 on 2018/2/27.
 */
public class ViewGroupCompat {
    public static void setTouchscreenBlocksFocus(ViewGroup view, boolean touchscreenBlocksFocus) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTouchscreenBlocksFocus(touchscreenBlocksFocus);
        }
    }
}
