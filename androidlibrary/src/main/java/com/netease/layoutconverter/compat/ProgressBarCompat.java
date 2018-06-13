package com.netease.layoutconverter.compat;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

/**
 * Created by hzwangpeng2015 on 2018/2/27.
 */
public class ProgressBarCompat {
    public static void setMin(ProgressBar progressBar, int min) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setMin(min);
        }
    }

    public static void setProgressTintMode(ProgressBar progressBar, @Nullable PorterDuff.Mode tintMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintMode(tintMode);
        }
    }

    public static void setProgressTintList(ProgressBar progressBar, @Nullable ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(tint);
        }
    }

    public static void setBackgroundTintMode(ProgressBar progressBar, @Nullable PorterDuff.Mode tintMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setBackgroundTintMode(tintMode);
        }
    }

    public static void setBackgroundTintList(ProgressBar progressBar, @Nullable ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setBackgroundTintList(tint);
        }
    }

    public static void setSecondaryProgressTintMode(ProgressBar progressBar, @Nullable PorterDuff.Mode tintMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setSecondaryProgressTintMode(tintMode);
        }
    }

    public static void setSecondaryProgressTintList(ProgressBar progressBar, @Nullable ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setSecondaryProgressTintList(tint);
        }
    }

    public static void setIndeterminateTintMode(ProgressBar progressBar, @Nullable PorterDuff.Mode tintMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setIndeterminateTintMode(tintMode);
        }
    }

    public static void setIndeterminateTintList(ProgressBar progressBar, @Nullable ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setIndeterminateTintList(tint);
        }
    }
}
