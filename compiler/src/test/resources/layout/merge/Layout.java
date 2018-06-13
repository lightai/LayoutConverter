package com.netease.layoutconverter.test;

import android.content.Context;
import android.view.*;
import android.widget.*;
import com.netease.layoutconverter.base.BaseLayout;
import com.netease.layoutconverter.test.R;

public class Layout extends BaseLayout {
    public static View inflate(Context context) {
        return inflate(context, null);
    }

    public static View inflate(Context context, ViewGroup root) {
        return inflate(context, root, true);
    }

    public static View inflate(Context context, ViewGroup root, boolean attachToRoot) {
        final LinearLayout linearLayout0 = new LinearLayout(context);
        ViewGroup.MarginLayoutParams lpLinearLayout0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        lpLinearLayout0.setMargins(dp(10, context), dp(10, context), dp(10, context), dp(10, context));
        linearLayout0.setLayoutParams(lpLinearLayout0);

        final TextView textView0 = new TextView(context);
        textView0.setBackground(0xffff0000);
        textView0.setEnabled(false);
        textView0.setGravity(View.Gravity.CENTER | View.Gravity.TOP);
        textView0.setHint("Hint");
        textView0.setId(R.id.textView1);
        textView0.setLines(5);
        textView0.setMargin(dp(10, context));
        textView0.setPaddingBottom(dp(4, context));
        textView0.setPaddingLeft(dp(1, context));
        textView0.setPaddingRight(dp(3, context));
        textView0.setPaddingTop(dp(2, context));
        textView0.setTag("Tag");
        textView0.setText("Text");
        textView0.setTextColor(0xff00ff00);
        textView0.setTextSize(sp(17, context));
        textView0.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams lpTextView0 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpTextView0.gravity=View.Gravity.CENTER;
        lpTextView0.setMargins(dp(10, context), dp(10, context), dp(10, context), dp(10, context));
        textView0.setLayoutParams(lpTextView0);
        linearLayout0.addView(textView0);

        final TextView textView1 = new TextView(context);
        textView1.setText("textView2");
        LinearLayout.LayoutParams lpTextView1 = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        lpTextView1.gravity=View.Gravity.CENTER;
        lpTextView1.setMargins(dp(10, context), dp(10, context), dp(10, context), dp(10, context));
        lpTextView1.weight=5;
        textView1.setLayoutParams(lpTextView1);
        linearLayout0.addView(textView1);

        if (attachToRoot) {
            root.addView(linearLayout0);
        }

        return linearLayout0;
    }
}