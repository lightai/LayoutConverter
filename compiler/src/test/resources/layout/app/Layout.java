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
        final LinearLayout linearLayout5 = obtainView(LinearLayout.class);
        linearLayout5.setBackground(ContextCompat.getDrawable(context, R.color.white));
        linearLayout5.setId(R.id.activity_account_bind);
        linearLayout5.setOrientation(android.widget.LinearLayout.VERTICAL);
        ViewGroup.MarginLayoutParams lpLinearLayout5 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        linearLayout5.setLayoutParams(lpLinearLayout5);

        final com.netease.moneykeeper.ui.common.SimpleActionBar simpleActionBar0 = obtainView(com.netease.moneykeeper.ui.common.SimpleActionBar.class);
        simpleActionBar0.setId(R.id.action_bar);
        // Can't find attr: actionBarTitle:解除帐号绑定
        // Can't find attr: defaultBack:true
        LinearLayout.LayoutParams lpSimpleActionBar0 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        simpleActionBar0.setLayoutParams(lpSimpleActionBar0);
        linearLayout5.addView(simpleActionBar0);

        final TextView textView0 = obtainView(TextView.class);
        textView0.setBackground(ContextCompat.getDrawable(context, R.color.bg_color));
        textView0.setGravity(Gravity.CENTER_VERTICAL);
        textView0.setId(R.id.hint);
        setViewPaddingLeft(textView0, (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        textView0.setText("请输入网易邮箱密码");
        LinearLayout.LayoutParams lpTextView0 = new LinearLayout.LayoutParams(MATCH_PARENT, dp(35, context));
        textView0.setLayoutParams(lpTextView0);
        linearLayout5.addView(textView0);

        final LinearLayout linearLayout0 = obtainView(LinearLayout.class);
        linearLayout0.setBackground(ContextCompat.getDrawable(context, R.color.bg_white));
        linearLayout0.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout0.setId(R.id.ll_account);
        linearLayout0.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        setViewPaddingLeft(linearLayout0, (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        setViewPaddingRight(linearLayout0, (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        LinearLayout.LayoutParams lpLinearLayout0 = new LinearLayout.LayoutParams(MATCH_PARENT, dp(50, context));
        linearLayout0.setLayoutParams(lpLinearLayout0);

        final EditText editText0 = obtainView(EditText.class);
        setTextViewDrawableLeft(editText0, ContextCompat.getDrawable(context, R.drawable.ic_login_account));
        editText0.setCompoundDrawablePadding(dp(9, context));
        editText0.setHint(context.getResources().getString(R.string.label_account));
        editText0.setId(R.id.login_account);
        editText0.setImeOptions(5);
        editText0.setRawInputType(33);
        editText0.setMaxLines(1);
        editText0.setTextColor(ContextCompat.getColorStateList(context, R.color.text_024));
        editText0.setHintTextColor(ContextCompat.getColorStateList(context, R.color.text_045));
        editText0.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) context.getResources().getDimension(R.dimen.text_045));
        LinearLayout.LayoutParams lpEditText0 = new LinearLayout.LayoutParams(dp(0, context), MATCH_PARENT);
        lpEditText0.weight=1f;
        editText0.setLayoutParams(lpEditText0);
        linearLayout0.addView(editText0);

        final ImageButton imageButton0 = obtainView(ImageButton.class);
        imageButton0.setId(R.id.login_account_clear);
        imageButton0.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams lpImageButton0 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        imageButton0.setLayoutParams(lpImageButton0);
        linearLayout0.addView(imageButton0);linearLayout5.addView(linearLayout0);

        final View view0 = obtainView(View.class);
        LinearLayout.LayoutParams lpView0 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpView0.leftMargin = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        lpView0.rightMargin = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        view0.setLayoutParams(lpView0);
        linearLayout5.addView(view0);

        final FrameLayout frameLayout0 = obtainView(FrameLayout.class);
        LinearLayout.LayoutParams lpFrameLayout0 = new LinearLayout.LayoutParams(MATCH_PARENT, dp(0, context));
        lpFrameLayout0.weight=1f;
        frameLayout0.setLayoutParams(lpFrameLayout0);

        final LinearLayout linearLayout3 = obtainView(LinearLayout.class);
        linearLayout3.setOrientation(android.widget.LinearLayout.VERTICAL);
        FrameLayout.LayoutParams lpLinearLayout3 = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        linearLayout3.setLayoutParams(lpLinearLayout3);

        final LinearLayout linearLayout1 = obtainView(LinearLayout.class);
        linearLayout1.setBackground(ContextCompat.getDrawable(context, R.color.bg_white));
        linearLayout1.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout1.setId(R.id.ll_password);
        linearLayout1.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        setViewPaddingLeft(linearLayout1, (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        setViewPaddingRight(linearLayout1, (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        LinearLayout.LayoutParams lpLinearLayout1 = new LinearLayout.LayoutParams(MATCH_PARENT, dp(54, context));
        linearLayout1.setLayoutParams(lpLinearLayout1);

        final EditText editText1 = obtainView(EditText.class);
        setTextViewDrawableLeft(editText1, ContextCompat.getDrawable(context, R.drawable.ic_login_password));
        editText1.setCompoundDrawablePadding(dp(9, context));
        editText1.setHint(context.getResources().getString(R.string.label_password));
        editText1.setId(R.id.login_password);
        editText1.setImeOptions(6);
        editText1.setRawInputType(129);
        editText1.setMaxLines(1);
        editText1.setTextColor(ContextCompat.getColorStateList(context, R.color.color_text_normal));
        editText1.setHintTextColor(ContextCompat.getColorStateList(context, R.color.text_045));
        editText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) context.getResources().getDimension(R.dimen.text_024));
        LinearLayout.LayoutParams lpEditText1 = new LinearLayout.LayoutParams(dp(0, context), MATCH_PARENT);
        lpEditText1.weight=1f;
        editText1.setLayoutParams(lpEditText1);
        linearLayout1.addView(editText1);

        final ImageButton imageButton1 = obtainView(ImageButton.class);
        imageButton1.setId(R.id.login_password_clear);
        imageButton1.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams lpImageButton1 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        imageButton1.setLayoutParams(lpImageButton1);
        linearLayout1.addView(imageButton1);linearLayout3.addView(linearLayout1);

        final View view1 = obtainView(View.class);
        LinearLayout.LayoutParams lpView1 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpView1.leftMargin = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        lpView1.rightMargin = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        view1.setLayoutParams(lpView1);
        linearLayout3.addView(view1);

        final Button button0 = obtainView(Button.class);
        button0.setId(R.id.remove_button);
        button0.setText("解除绑定");
        LinearLayout.LayoutParams lpButton0 = new LinearLayout.LayoutParams(MATCH_PARENT, dp(45, context));
        lpButton0.leftMargin = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        lpButton0.rightMargin = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        lpButton0.topMargin = dp(40, context);
        button0.setLayoutParams(lpButton0);
        linearLayout3.addView(button0);

        final android.support.v4.widget.Space space0 = obtainView(android.support.v4.widget.Space.class);
        LinearLayout.LayoutParams lpSpace0 = new LinearLayout.LayoutParams(MATCH_PARENT, dp(0, context));
        lpSpace0.weight=1f;
        space0.setLayoutParams(lpSpace0);
        linearLayout3.addView(space0);

        final LinearLayout linearLayout2 = obtainView(LinearLayout.class);
        linearLayout2.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lpLinearLayout2 = new LinearLayout.LayoutParams(MATCH_PARENT, dp(40, context));
        lpLinearLayout2.bottomMargin = dp(30, context);
        lpLinearLayout2.topMargin = dp(15, context);
        linearLayout2.setLayoutParams(lpLinearLayout2);

        final View view2 = obtainView(View.class);
        LinearLayout.LayoutParams lpView2 = new LinearLayout.LayoutParams(dp(0, context), MATCH_PARENT);
        lpView2.weight=1f;
        view2.setLayoutParams(lpView2);
        linearLayout2.addView(view2);

        final TextView textView1 = obtainView(TextView.class);
        textView1.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        textView1.setId(R.id.tv_forget_password);
        textView1.setText(context.getResources().getString(R.string.label_forget_password));
        LinearLayout.LayoutParams lpTextView1 = new LinearLayout.LayoutParams(dp(80, context), MATCH_PARENT);
        textView1.setLayoutParams(lpTextView1);
        linearLayout2.addView(textView1);

        final View view3 = obtainView(View.class);
        view3.setBackground(ContextCompat.getDrawable(context, R.color.list_view_divider));
        LinearLayout.LayoutParams lpView3 = new LinearLayout.LayoutParams(dp(1, context), dp(13, context));
        lpView3.gravity=Gravity.CENTER_VERTICAL;
        lpView3.leftMargin = (int) context.getResources().getDimension(R.dimen.dp15);
        lpView3.rightMargin = (int) context.getResources().getDimension(R.dimen.dp15);
        view3.setLayoutParams(lpView3);
        linearLayout2.addView(view3);

        final TextView textView2 = obtainView(TextView.class);
        textView2.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView2.setId(R.id.tv_register);
        textView2.setText("注册网易帐号");
        LinearLayout.LayoutParams lpTextView2 = new LinearLayout.LayoutParams(dp(80, context), MATCH_PARENT);
        textView2.setLayoutParams(lpTextView2);
        linearLayout2.addView(textView2);

        final View view4 = obtainView(View.class);
        LinearLayout.LayoutParams lpView4 = new LinearLayout.LayoutParams(dp(0, context), MATCH_PARENT);
        lpView4.weight=1f;
        view4.setLayoutParams(lpView4);
        linearLayout2.addView(view4);linearLayout3.addView(linearLayout2);frameLayout0.addView(linearLayout3);

        final LinearLayout linearLayout4 = obtainView(LinearLayout.class);
        linearLayout4.setBackground(ContextCompat.getDrawable(context, R.color.bg_white));
        linearLayout4.setId(R.id.ll_account_hint);
        linearLayout4.setOrientation(android.widget.LinearLayout.VERTICAL);
        setViewPaddingLeft(linearLayout4, dp(35, context));
        setViewPaddingRight(linearLayout4, dp(35, context));
        linearLayout4.setVisibility(View.GONE);
        FrameLayout.LayoutParams lpLinearLayout4 = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        linearLayout4.setLayoutParams(lpLinearLayout4);
        frameLayout0.addView(linearLayout4);linearLayout5.addView(frameLayout0);

        onFinishInflate();

        if (attachToRoot) {
            root.addView(linearLayout5);
        }

        return linearLayout5;
    }
}