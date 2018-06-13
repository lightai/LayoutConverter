package com.netease.layoutconverter.test;

import android.content.Context;
import android.view.*;
import android.widget.*;
import android.webkit.*;
import android.text.*;
import android.graphics.drawable.*;
import android.content.res.*;
import android.animation.*;
import com.netease.layoutconverter.test.R;
import android.support.v4.content.ContextCompat;
import android.util.*;
import com.netease.layoutconverter.BaseLayout;
import com.netease.layoutconverter.compat.*;
import android.support.design.widget.*;

public class Layout extends BaseLayout {
    public static Layout from(Context context) {
        return new Layout(context);
    }

    private Layout(Context context) {
        super(context);
    }

    @Override public View inflate(ViewGroup root, boolean attachToRoot) {
        final LinearLayout linearLayout0 = obtainView(LinearLayout.class);
        ViewGroup.MarginLayoutParams lpLinearLayout0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        lpLinearLayout0.setMargins(dp(10, context), dp(10, context), dp(10, context), dp(10, context));
        linearLayout0.setLayoutParams(lpLinearLayout0);

        final TextView textView0 = obtainView(TextView.class);
        textView0.setId(R.id.include);
        textView0.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lpTextView0 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textView0.setLayoutParams(lpTextView0);
        linearLayout0.addView(textView0);

        final View view0 = obtainView(View.class);
        LinearLayout.LayoutParams lpView0 = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        view0.setLayoutParams(lpView0);
        linearLayout0.addView(view0);

        onFinishInflate();

        if (attachToRoot) {
            root.addView(linearLayout0);
        }

        return linearLayout0;
    }
}