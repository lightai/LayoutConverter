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
        final ImageView imageView0 = obtainView(ImageView.class);
        imageView0.setAdjustViewBounds(true);
        imageView0.setBaseline(dp(1, context));
        imageView0.setBaselineAlignBottom(true);
        imageView0.setCropToPadding(true);
        imageView0.setScaleType(ImageView.ScaleType.CENTER);
        imageView0.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background));
        ViewGroup.MarginLayoutParams lpImageView0 = new ViewGroup.MarginLayoutParams(dp(50, context), dp(50, context));
        imageView0.setLayoutParams(lpImageView0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(imageView0);
        }

        return imageView0;
    }
}