package com.netease.layoutconverter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * View池中view的Context
 * Created by hzwangpeng2015 on 2017/12/9.
 */
public class ViewContext extends ContextWrapper {
    static final String TAG = "ViewContext";

    /**
     * view当前运行环境的context
     */
    private WeakReference<Context> currContextRef;

    /**
     * @param realContext View当前运行环境的Context
     */
    public ViewContext(Context realContext) {
        super(realContext.getApplicationContext());
        this.currContextRef = new WeakReference<>(realContext);
    }

    public void setCurrentContext(Context context) {
        currContextRef = new WeakReference<>(context);
    }

    public Context getCurrentContext() {
        return currContextRef != null ? currContextRef.get() : null;
    }

    @Override
    public void startActivity(Intent intent) {
        Context realContext = currContextRef != null ? currContextRef.get() : null;
        if (realContext != null) {
            realContext.startActivity(intent);
        } else {
            super.startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivity(Intent intent, Bundle options) {
        Context realContext = currContextRef != null ? currContextRef.get() : null;
        if (realContext != null) {
            realContext.startActivity(intent, options);
        } else {
            super.startActivity(intent, options);
        }
    }

    @Override
    public void startActivities(Intent[] intents) {
        Context realContext = currContextRef != null ? currContextRef.get() : null;
        if (realContext != null) {
            realContext.startActivities(intents);
        } else {
            super.startActivities(intents);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivities(Intent[] intents, Bundle options) {
        Context realContext = currContextRef != null ? currContextRef.get() : null;
        if (realContext != null) {
            realContext.startActivities(intents, options);
        } else {
            super.startActivities(intents, options);
        }
    }

    @Override
    public Resources getResources() {
        Context realContext = currContextRef != null ? currContextRef.get() : null;
        if (realContext != null) {
            return realContext.getResources();
        } else {
            return super.getResources();
        }
    }

    @Override
    public Resources.Theme getTheme() {
        Context realContext = currContextRef != null ? currContextRef.get() : null;
        if (realContext != null) {
            return realContext.getTheme();
        } else {
            return super.getTheme();
        }
    }


    @Nullable
    public static Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }

        if (context instanceof ViewContext) {
            Context currContext = ((ViewContext) context).getCurrentContext();
            if (currContext instanceof Activity) {
                return (Activity) currContext;
            }
        }

        return null;
    }
}
