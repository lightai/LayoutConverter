package com.netease.layoutconverter.compat;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by hzwangpeng2015 on 2018/2/27.
 */
public class ViewCompat {
    public static void setContextClickable(View view, boolean contextClickable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setContextClickable(contextClickable);
        }
    }

    public static void setAccessibilityTraversalBefore(View view, int beforeId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            view.setAccessibilityTraversalBefore(beforeId);
        }
    }

    public static void setAccessibilityTraversalAfter(View view, int beforeId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            view.setAccessibilityTraversalAfter(beforeId);
        }
    }

    public static void setTextDirection(View view, int textDirection) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setTextDirection(textDirection);
        }
    }

    public static void setOutlineProvider(View view, ViewOutlineProvider provider) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setOutlineProvider(provider);
        }
    }

    public static void setForegroundTintList(View view, @Nullable ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setForegroundTintList(tint);
        }
    }

    public static void setForegroundTintMode(View view, @Nullable PorterDuff.Mode tintMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setForegroundTintMode(tintMode);
        }
    }

    public static void forceHasOverlappingRendering(View view, boolean hasOverlappingRendering) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.forceHasOverlappingRendering(hasOverlappingRendering);
        }
    }

    public static void setDefaultFocusHighlightEnabled(View view, boolean defaultFocusHighlightEnabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            view.setDefaultFocusHighlightEnabled(defaultFocusHighlightEnabled);
        }
    }
}
