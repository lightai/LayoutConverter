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
        final View view0 = obtainView(View.class);
        view0.setAccessibilityLiveRegion(2);
        view0.setAccessibilityTraversalAfter(R.id.view);
        view0.setAccessibilityTraversalBefore(R.id.view);
        view0.setAlpha(1f);
        // Can't find attr: autofillHints:@string/app_name
        view0.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_launcher_background));
        view0.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        view0.setBackgroundTintMode(intToMode(9));
        view0.setClickable(true);
        view0.setContentDescription(context.getResources().getString(R.string.app_name));
        view0.setContextClickable(true);
        // Can't find attr: defaultFocusHighlightEnabled:true
        view0.setDrawingCacheQuality(0);
        view0.setDuplicateParentStateEnabled(true);
        view0.setElevation(dp(0, context));
        // Ignore fadingEdge
        view0.setFilterTouchesWhenObscured(true);
        view0.setFitsSystemWindows(true);
        view0.setFocusable(true);
        view0.setFocusableInTouchMode(true);
        // Can't find attr: focusedByDefault:true
        view0.forceHasOverlappingRendering(true);
        view0.setForeground(ContextCompat.getDrawable(context, R.color.colorPrimary));
        view0.setForegroundGravity(17);
        view0.setForegroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        view0.setForegroundTintMode(intToMode(9));
        view0.setHapticFeedbackEnabled(true);
        view0.setId(R.id.view);
        view0.setImportantForAccessibility(0);
        // Can't find attr: importantForAutofill:auto
        view0.setScrollContainer(true);
        view0.setKeepScreenOn(true);
        // Can't find attr: keyboardNavigationCluster:true
        view0.setLabelFor(R.id.view);
        view0.setLayerType(2, null);
        view0.setLayoutDirection(2);
        view0.setLongClickable(true);
        view0.setMinimumHeight(dp(20, context));
        view0.setMinimumWidth(dp(20, context));
        view0.setNestedScrollingEnabled(true);
        view0.setNextFocusLeftId(R.id.view);
        view0.setOnClickListener(new DeclaredOnClickListener(view0, "onClick"));
        view0.setOutlineProvider(getOutlineProviderFromAttribute(0));
        view0.setOverScrollMode(0);
        setViewPaddingBottom(view0, dp(15, context));
        setViewPaddingLeft(view0, dp(15, context));
        setViewPaddingRight(view0, dp(15, context));
        setViewPaddingTop(view0, dp(15, context));
        view0.setPointerIcon(PointerIcon.load(context.getResources(), 1008));
        view0.setRequiresFadingEdge(4096);
        view0.setRotation(0dpf);
        view0.setRotationX(0dpf);
        view0.setRotationY(0dpf);
        view0.setSaveEnabled(true);
        view0.setScaleY(1dpf);
        view0.setScrollIndicators(2);
        view0.setScrollX(dp(1, context));
        view0.setScrollBarStyle(0x01000000);
        // Ignore scrollbars
        view0.setSoundEffectsEnabled(true);
        view0.setStateListAnimator(AnimatorInflater.loadStateListAnimator(context, R.string.app_name));
        view0.setTag("tag");
        view0.setTextAlignment(0);
        view0.setTextDirection(0);
        // Can't find attr: tooltipText:@string/app_name
        view0.setPivotX(dp(0, context));
        view0.setPivotY(dp(0, context));
        view0.setTransitionName(context.getResources().getString(R.string.app_name));
        view0.setTranslationX(dp(0, context));
        view0.setTranslationY(dp(0, context));
        view0.setTranslationZ(dp(0, context));
        view0.setVerticalScrollbarPosition(0);
        view0.setVisibility(View.VISIBLE);
        ViewGroup.MarginLayoutParams lpView0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        view0.setLayoutParams(lpView0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(view0);
        }

        return view0;
    }
}