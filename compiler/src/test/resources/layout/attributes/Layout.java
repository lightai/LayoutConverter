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
        final LinearLayout linearLayout0 = obtainView(LinearLayout.class);
        ViewGroup.MarginLayoutParams lpLinearLayout0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        lpLinearLayout0.setMargins(dp(10, context), dp(10, context), dp(10, context), dp(10, context));
        linearLayout0.setLayoutParams(lpLinearLayout0);

        final TextView textView0 = obtainView(TextView.class);
        textView0.setBackground(new ColorDrawable(0xffff0000));
        textView0.setEllipsize(TextUtils.TruncateAt.END);
        textView0.setEnabled(false);
        textView0.setGravity(Gravity.CENTER | Gravity.TOP);
        textView0.setHint("Hint");
        textView0.setId(R.id.textView1);
        textView0.setLines(5);
        // Can't find attr: margin:10dp
        setViewPaddingBottom(textView0, dp(4, context));
        setViewPaddingLeft(textView0, dp(1, context));
        setViewPaddingRight(textView0, dp(3, context));
        setViewPaddingTop(textView0, dp(2, context));
        textView0.setTag("Tag");
        textView0.setText("Text");
        textView0.setTextColor(ColorStateList.valueOf(0xff00ff00));
        textView0.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp(17, context));
        setTextViewStyle(textView0, 1);
        textView0.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams lpTextView0 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpTextView0.gravity=Gravity.CENTER;
        lpTextView0.setMargins(dp(10, context), dp(10, context), dp(10, context), dp(10, context));
        textView0.setLayoutParams(lpTextView0);
        linearLayout0.addView(textView0);

        final TextView textView1 = obtainView(TextView.class);
        textView1.setText("textView2");
        LinearLayout.LayoutParams lpTextView1 = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        lpTextView1.gravity=Gravity.CENTER;
        lpTextView1.setMargins(dp(10, context), dp(10, context), dp(10, context), dp(10, context));
        lpTextView1.weight=5f;
        textView1.setLayoutParams(lpTextView1);
        linearLayout0.addView(textView1);

        onFinishInflate();

        if (attachToRoot) {
            root.addView(linearLayout0);
        }

        return linearLayout0;
    }
}