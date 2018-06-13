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
        final ProgressBar progressBar0 = obtainView(ProgressBar.class);
        progressBar0.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        progressBar0.setBackgroundTintMode(intToMode(9));
        progressBar0.setIndeterminate(true);
        set_ProgressBar_mBehavior(progressBar0, 2);
        progressBar0.setIndeterminateDrawable(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background));
        set_ProgressBar_mDuration(progressBar0, 300);
        progressBar0.setIndeterminate(true);
        progressBar0.setIndeterminateTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        progressBar0.setIndeterminateTintMode(intToMode(9));
        progressBar0.setInterpolator(context, android.R.anim.linear_interpolator);
        progressBar0.setMax(100);
        setProgressBarMaxHeight(progressBar0, dp(50, context));
        setProgressBarMaxWidth(progressBar0, dp(50, context));
        progressBar0.setMin(0);
        setProgressBarMinHeight(progressBar0, dp(10, context));
        setProgressBarMinWidth(progressBar0, dp(10, context));
        progressBar0.setProgress(50);
        progressBar0.setProgressDrawable(ContextCompat.getDrawable(context, R.color.colorPrimary));
        progressBar0.setProgressTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        progressBar0.setProgressTintMode(intToMode(9));
        progressBar0.setSecondaryProgress(50);
        progressBar0.setSecondaryProgressTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        progressBar0.setSecondaryProgressTintMode(intToMode(9));
        ViewGroup.MarginLayoutParams lpProgressBar0 = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        progressBar0.setLayoutParams(lpProgressBar0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(progressBar0);
        }

        return progressBar0;
    }
}