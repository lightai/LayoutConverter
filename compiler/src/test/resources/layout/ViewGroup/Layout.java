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
        final ViewGroup viewGroup0 = obtainView(ViewGroup.class);
        viewGroup0.setAddStatesFromChildren(true);
        viewGroup0.setAlwaysDrawnWithCacheEnabled(true);
        if (true) { viewGroup0.setLayoutTransition(new LayoutTransition()); }
        viewGroup0.setAnimationCacheEnabled(true);
        viewGroup0.setClipChildren(true);
        viewGroup0.setClipToPadding(true);
        viewGroup0.setDescendantFocusability(1);
        viewGroup0.setLayoutAnimation(android.view.animation.AnimationUtils.loadLayoutAnimation(context, android.R.anim.accelerate_decelerate_interpolator));
        viewGroup0.setLayoutMode(0);
        viewGroup0.setPersistentDrawingCache(3);
        viewGroup0.setMotionEventSplittingEnabled(true);
        viewGroup0.setTouchscreenBlocksFocus(true);
        viewGroup0.setTransitionGroup(true);
        ViewGroup.MarginLayoutParams lpViewGroup0 = new ViewGroup.MarginLayoutParams(MATCH_PARENT, MATCH_PARENT);
        viewGroup0.setLayoutParams(lpViewGroup0);


        onFinishInflate();

        if (attachToRoot) {
            root.addView(viewGroup0);
        }

        return viewGroup0;
    }
}