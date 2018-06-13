package com.netease.layoutconverter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.XmlRes;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static android.util.TypedValue.TYPE_DIMENSION;
import static android.util.TypedValue.TYPE_REFERENCE;
import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;
import static android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS;
import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

/**
 * Created by hzwangpeng2015 on 2017/11/30.
 */
public abstract class BaseLayout {
    private static final String TAG = "BaseLayout";

    protected static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    private static final SparseArray<Method> methodCache = new SparseArray<>();
    private static final Method NO_METHOD = BaseLayout.class.getMethods()[0];

    private static DisplayMetrics metrics;
    private static DisplayMetrics metrics(Context context) {
        if (metrics == null) {
            metrics = context.getResources().getDisplayMetrics();
        }
        return metrics;
    }

    protected final Context context;

    protected BaseLayout(Context context) {
        this.context = context;
    }

    public View inflate() {
        return inflate(null);
    }

    public View inflate(ViewGroup root) {
        return inflate(root, root != null);
    }

    public abstract View inflate(ViewGroup root, boolean attachToRoot);

    protected void onFinishInflate() {
        // todo: 优化性能
        if (tmpMap != null) {
            for (Map.Entry<View, SparseArray<Object>> entry : tmpMap.entrySet()) {
                final View view = entry.getKey();
                final SparseArray<Object> map = entry.getValue();
                handleViewPadding(view, map);

                if (view instanceof TextView) {
                    handleTextViewDrawable((TextView) view, map);
                    handleTextViewTypeface((TextView) view, map);
                    handleTextViewExtra((TextView) view, map);
                    handleTextViewShadow((TextView) view, map);
                    handleTextViewImeActionLabel((TextView) view, map);
                } else if (view instanceof ExpandableListView) {
                    handleExpandableListView((ExpandableListView) view, map);
                }
            }
        }

        // gc
        // todo: bg thread clear
        tmpMap = null;
    }

    protected <T extends View> T obtainView(Class<T> cls) {
        return ViewFactory.getInstance().obtainView(context, cls);
    }

    protected <T extends View> T obtainView(Class<T> cls, int theme) {
        return ViewFactory.getInstance().createView(cls, new ContextThemeWrapper(context, theme));
    }

    /*-- 数据转换 --*/
    protected static int dp(float v, Context context) {
        return (int) (v * metrics(context).density);
    }
    protected static int sp(float v, Context context) {
        return (int) (v * metrics(context).scaledDensity);
    }
    protected static int mm(float v, Context context) {
        return (int) (v * metrics(context).xdpi * (1.0f/25.4f));
    }
    protected static int in(float v, Context context) {
        return (int) (v * metrics(context).xdpi);
    }
    protected static int pt(float v, Context context) {
        return (int) (v * metrics(context).xdpi * (1.0f/72));
    }

    private void reflectSet(View view, String fieldName, int styleableId, Object value) {
        Field field = fieldCache.get(styleableId);
        try {
            if (field == null) {
                Field field1 = ProgressBar.class.getDeclaredField(fieldName);
                field1.setAccessible(true);
                fieldCache.put(styleableId, field1);
                field = field1;
            }
            field.set(view, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static final int PROVIDER_BACKGROUND = 0;
    private static final int PROVIDER_NONE = 1;
    private static final int PROVIDER_BOUNDS = 2;
    private static final int PROVIDER_PADDED_BOUNDS = 3;
    @SuppressLint("NewApi")
    protected ViewOutlineProvider getOutlineProviderFromAttribute(int providerInt) {
        switch (providerInt) {
            case PROVIDER_BACKGROUND:
                return ViewOutlineProvider.BACKGROUND;
            case PROVIDER_NONE:
                return null;
            case PROVIDER_BOUNDS:
                return ViewOutlineProvider.BOUNDS;
            case PROVIDER_PADDED_BOUNDS:
                return ViewOutlineProvider.PADDED_BOUNDS;
        }
        return null;
    }

    /**
     * Parses a {@link android.graphics.PorterDuff.Mode} from a tintMode
     * attribute's enum value.
     *
     */
    private PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3: return PorterDuff.Mode.SRC_OVER;
            case 5: return PorterDuff.Mode.SRC_IN;
            case 9: return PorterDuff.Mode.SRC_ATOP;
            case 14: return PorterDuff.Mode.MULTIPLY;
            case 15: return PorterDuff.Mode.SCREEN;
            case 16: return PorterDuff.Mode.ADD;
            default: return defaultMode;
        }
    }

    protected static PorterDuff.Mode intToMode(int val) {
        switch (val) {
            default:
            case  0: return PorterDuff.Mode.CLEAR;
            case  1: return PorterDuff.Mode.SRC;
            case  2: return PorterDuff.Mode.DST;
            case  3: return PorterDuff.Mode.SRC_OVER;
            case  4: return PorterDuff.Mode.DST_OVER;
            case  5: return PorterDuff.Mode.SRC_IN;
            case  6: return PorterDuff.Mode.DST_IN;
            case  7: return PorterDuff.Mode.SRC_OUT;
            case  8: return PorterDuff.Mode.DST_OUT;
            case  9: return PorterDuff.Mode.SRC_ATOP;
            case 10: return PorterDuff.Mode.DST_ATOP;
            case 11: return PorterDuff.Mode.XOR;
            case 16: return PorterDuff.Mode.DARKEN;
            case 17: return PorterDuff.Mode.LIGHTEN;
            case 13: return PorterDuff.Mode.MULTIPLY;
            case 14: return PorterDuff.Mode.SCREEN;
            case 12: return PorterDuff.Mode.ADD;
            case 15: return PorterDuff.Mode.OVERLAY;
        }
    }

    /*-- view属性设置 --*/
    private Map<View, SparseArray<Object>> tmpMap;

    /**
     * 设置view的padding属性
     */
    private void handleViewPadding(View view, SparseArray<Object> map) {
        int paddingLeft, paddingRight, paddingTop, paddingBottom;
        if (map.indexOfKey(RR.styleable.View_padding) >= 0) {
            final int padding = (int) map.get(RR.styleable.View_padding);
            paddingLeft = padding;
            paddingRight = padding;
            paddingTop = padding;
            paddingBottom = padding;
        } else {
            if (map.indexOfKey(RR.styleable.View_paddingLeft) >= 0) {
                paddingLeft = (int) map.get(RR.styleable.View_paddingLeft);
            } else {
                paddingLeft = 0;
            }
            if (map.indexOfKey(RR.styleable.View_paddingRight) >= 0) {
                paddingRight = (int) map.get(RR.styleable.View_paddingRight);
            } else {
                paddingRight = 0;
            }
            if (map.indexOfKey(RR.styleable.View_paddingTop) >= 0) {
                paddingTop = (int) map.get(RR.styleable.View_paddingTop);
            } else {
                paddingTop = 0;
            }
            if (map.indexOfKey(RR.styleable.View_paddingBottom) >= 0) {
                paddingBottom = (int) map.get(RR.styleable.View_paddingBottom);
            } else {
                paddingBottom = 0;
            }
        }
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    private SparseArray<Object> getViewMap(View view) {
        if (tmpMap == null) {
            tmpMap = new HashMap<>();
        }

        SparseArray<Object> tag = tmpMap.get(view);
        if (tag == null) {
            tmpMap.put(view, tag = new SparseArray<>());
        }

        return tag;
    }

    protected void setViewPadding(View view, int padding) {
        getViewMap(view).put(RR.styleable.View_padding, padding);
    }
    protected void setViewPaddingLeft(View view, int paddingLeft) {
        getViewMap(view).put(RR.styleable.View_paddingLeft, paddingLeft);
    }
    protected void setViewPaddingRight(View view, int paddingRight) {
        getViewMap(view).put(RR.styleable.View_paddingRight, paddingRight);
    }
    protected void setViewPaddingTop(View view, int paddingTop) {
        getViewMap(view).put(RR.styleable.View_paddingTop, paddingTop);
    }
    protected void setViewPaddingBottom(View view, int paddingBottom) {
        getViewMap(view).put(RR.styleable.View_paddingBottom, paddingBottom);
    }

    // ViewGroup
    /**
     * Used to map between enum in attrubutes and flag values.
     */
    protected static final int[] DESCENDANT_FOCUSABILITY_FLAGS =
            {FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS,
                    FOCUS_BLOCK_DESCENDANTS};

    // TextView Drawable
    protected void setTextViewDrawableLeft(TextView view, Drawable drawable) {
        getViewMap(view).put(RR.styleable.TextView_drawableLeft, drawable);
    }
    protected void setTextViewDrawableRight(TextView view, Drawable drawable) {
        getViewMap(view).put(RR.styleable.TextView_drawableRight, drawable);
    }
    protected void setTextViewDrawableTop(TextView view, Drawable drawable) {
        getViewMap(view).put(RR.styleable.TextView_drawableTop, drawable);
    }
    protected void setTextViewDrawableBottom(TextView view, Drawable drawable) {
        getViewMap(view).put(RR.styleable.TextView_drawableBottom, drawable);
    }
    protected void setTextViewDrawableStart(TextView view, Drawable drawable) {
        getViewMap(view).put(RR.styleable.TextView_drawableStart, drawable);
    }
    protected void setTextViewDrawableEnd(TextView view, Drawable drawable) {
        getViewMap(view).put(RR.styleable.TextView_drawableEnd, drawable);
    }

    @SuppressLint("PrivateApi")
    private void handleTextViewDrawable(TextView view, SparseArray<Object> map) {
        Drawable left = null, right = null, top = null, bottom = null;
        Drawable drawableStart = null, drawableEnd = null;

        if (map.indexOfKey(RR.styleable.TextView_drawableLeft) >= 0) {
            left = (Drawable) map.get(RR.styleable.TextView_drawableLeft);
        }
        if (map.indexOfKey(RR.styleable.TextView_drawableRight) >= 0) {
            right = (Drawable) map.get(RR.styleable.TextView_drawableRight);
        }
        if (map.indexOfKey(RR.styleable.TextView_drawableTop) >= 0) {
            top = (Drawable) map.get(RR.styleable.TextView_drawableTop);
        }
        if (map.indexOfKey(RR.styleable.TextView_drawableBottom) >= 0) {
            bottom = (Drawable) map.get(RR.styleable.TextView_drawableBottom);
        }

	    if (map.indexOfKey(RR.styleable.TextView_drawableStart) >= 0) {
		    drawableStart = (Drawable) map.get(RR.styleable.TextView_drawableStart);
	    }
	    if (map.indexOfKey(RR.styleable.TextView_drawableEnd) >= 0) {
		    drawableEnd = (Drawable) map.get(RR.styleable.TextView_drawableStart);
	    }

	    view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);

        if (drawableStart != null || drawableEnd != null) {
            try {
                Method method = methodCache.get(RR.styleable.TextView_drawableStart);
                if (method == null) {
                    try {
                        method = view.getClass().getDeclaredMethod("setRelativeDrawablesIfNeeded", Drawable.class, Drawable.class);
                        method.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        methodCache.put(RR.styleable.TextView_drawableStart, method = NO_METHOD);
                    }
                }
                if (method != NO_METHOD) {
                    method.invoke(view, drawableStart, drawableEnd);
                }
            }  catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    protected void setTextViewShadowColor(TextView view, int value) {
	    getViewMap(view).put(RR.styleable.TextView_shadowColor, value);
    }
	protected void setTextViewShadowDx(TextView view, float value) {
		getViewMap(view).put(RR.styleable.TextView_shadowDx, value);
	}
	protected void setTextViewShadowDy(TextView view, float value) {
		getViewMap(view).put(RR.styleable.TextView_shadowDy, value);
	}
	protected void setTextViewShadowRadius(TextView view, float value) {
		getViewMap(view).put(RR.styleable.TextView_shadowRadius, value);
	}
	private void handleTextViewShadow(TextView view, SparseArray<Object> map) {
		int shadowcolor = 0;
		float dx = 0, dy = 0, r = 0;
		if (map.indexOfKey(RR.styleable.TextView_shadowColor) >= 0) {
			shadowcolor = (int) map.get(RR.styleable.TextView_shadowColor);
		}
		if (map.indexOfKey(RR.styleable.TextView_shadowDx) >= 0) {
			dx = (float) map.get(RR.styleable.TextView_shadowDx);
		}
		if (map.indexOfKey(RR.styleable.TextView_shadowDy) >= 0) {
			dy = (float) map.get(RR.styleable.TextView_shadowDy);
		}
		if (map.indexOfKey(RR.styleable.TextView_shadowRadius) >= 0) {
			r = (float) map.get(RR.styleable.TextView_shadowRadius);
		}

		if (shadowcolor != 0) {
			view.setShadowLayer(r, dx, dy, shadowcolor);
		}
	}

	protected void setTextViewImeActionLabel(TextView view, CharSequence value) {
		getViewMap(view).put(RR.styleable.TextView_imeActionLabel, value);
	}
	protected void setTextViewImeActionId(TextView view, int value) {
		getViewMap(view).put(RR.styleable.TextView_imeActionId, value);
	}
	private void handleTextViewImeActionLabel(TextView view, SparseArray<Object> map) {
		CharSequence imeActionLabel = view.getImeActionLabel();
    	int imeActionId = view.getImeActionId();
		if (map.indexOfKey(RR.styleable.TextView_imeActionLabel) >= 0) {
			imeActionLabel = (CharSequence) map.get(RR.styleable.TextView_imeActionLabel);
		}
		if (map.indexOfKey(RR.styleable.TextView_imeActionId) >= 0) {
			imeActionId = (int) map.get(RR.styleable.TextView_imeActionId);
		}
		view.setImeActionLabel(imeActionLabel, imeActionId);
	}
	protected void setTextViewInputExtras(TextView view, @XmlRes int xmlResId) {
		try {
			view.setInputExtras(xmlResId);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    // TextView type face
    protected void setTextViewStyle(TextView view, int style) {
        getViewMap(view).put(RR.styleable.TextView_textStyle, style);
    }
    protected void setTextViewTypeface(TextView view, int typeface) {
        getViewMap(view).put(RR.styleable.TextView_typeface, typeface);
    }
    private void handleTextViewTypeface(TextView view, SparseArray<Object> map) {
        int style = -1;
        Typeface typeface = Typeface.DEFAULT;
        if (map.indexOfKey(RR.styleable.TextView_textStyle) >= 0) {
            style = (int) map.get(RR.styleable.TextView_textStyle);
        }
        if (map.indexOfKey(RR.styleable.TextView_typeface) >= 0) {
            int t = (int) map.get(RR.styleable.TextView_typeface);
            if (t == 0) {
                typeface = Typeface.DEFAULT;
            } else if (t == 1) {
                typeface = Typeface.SANS_SERIF;
            } else if (t == 2) {
                typeface = Typeface.SERIF;
            } else if (t == 3) {
                typeface = Typeface.MONOSPACE;
            }
        }
        if (typeface != null || style >= 0) {
            if (style == -1) { style = 0; }
            view.setTypeface(typeface, style);
        }
    }

    // TextView line space
    protected void setTextViewLineSpaceExtra(TextView view, float v) {
        getViewMap(view).put(RR.styleable.TextView_lineSpacingExtra, v);
    }
    protected void setTextViewLineSpaceMultiplier(TextView view, float v) {
        getViewMap(view).put(RR.styleable.TextView_lineSpacingMultiplier, v);
    }
    private void handleTextViewExtra(TextView view, SparseArray<Object> map) {
        float spacingMult = 1.0f;
        float spacingAdd = 0.0f;
        if (map.indexOfKey(RR.styleable.TextView_lineSpacingExtra) >= 0) {
            spacingAdd = (float) map.get(RR.styleable.TextView_lineSpacingExtra);
        }
        if (map.indexOfKey(RR.styleable.TextView_lineSpacingMultiplier) >= 0) {
            spacingMult = (float) map.get(RR.styleable.TextView_lineSpacingMultiplier);
        }
        view.setLineSpacing(spacingAdd, spacingMult);
    }
    protected void setTextViewDrawableTintMode(TextView view, int value) {
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		    view.setCompoundDrawableTintMode(parseTintMode(value, view.getCompoundDrawableTintMode()));
	    }
    }
    protected void setTextViewBufferType(TextView view, int value) {
	    TextView.BufferType bufferType = TextView.BufferType.EDITABLE;
	    switch (value) {
		    case 0:
			    bufferType = TextView.BufferType.NORMAL;
			    break;
		    case 1:
			    bufferType = TextView.BufferType.SPANNABLE;
			    break;
		    case 2:
			    bufferType = TextView.BufferType.EDITABLE;
			    break;
	    }
	    view.setText(view.getText(), bufferType);
    }

    protected void set_TextView_mCursorDrawableRes(TextView textView, int id) {
        reflectSet(textView, "mCursorDrawableRes", RR.styleable.TextView_textCursorDrawable, id);
    }
    protected void set_TextView_mTextSelectHandleLeftRes(TextView textView, int id) {
        reflectSet(textView, "mTextSelectHandleLeftRes", RR.styleable.TextView_textSelectHandleLeft, id);
    }
    protected void set_TextView_mTextSelectHandleRightRes(TextView textView, int id) {
        reflectSet(textView, "mTextSelectHandleRightRes", RR.styleable.TextView_textSelectHandleRight, id);
    }
    protected void set_TextView_mTextSelectHandleRes(TextView textView, int id) {
        reflectSet(textView, "mTextSelectHandleRes", RR.styleable.TextView_textSelectHandle, id);
    }
    protected void set_TextView_mTextEditSuggestionItemLayout(TextView textView, int id) {
        reflectSet(textView, "mTextEditSuggestionItemLayout", RR.styleable.TextView_textEditSuggestionItemLayout, id);
    }
    protected void set_TextView_mTextEditSuggestionContainerLayout(TextView textView, int id) {
        reflectSet(textView, "mTextEditSuggestionContainerLayout", "RR.styleable.TextView_textEditSuggestionContainerLayout".hashCode(), id);
    }
    protected void set_TextView_mTextEditSuggestionHighlightStyle(TextView textView, int id) {
        reflectSet(textView, "mTextEditSuggestionHighlightStyle", "RR.styleable.TextView_textEditSuggestionHighlightStyle".hashCode(), id);
    }

    /*-- SearchView --*/
    protected void setSearchViewCloseIconImageDrawable(View view, Drawable drawable) {
//        ImageView imageView = view.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
//        if (imageView != null) {
//            imageView.setImageDrawable(drawable);
//        }
//        ImageView imageView2 = view.findViewById(R.id.search_close_btn);
//        if (imageView2 != null) {
//            imageView2.setImageDrawable(drawable);
//        }
    }

    /*-- ProgressBar --*/
    private static SparseArray<Field> fieldCache = new SparseArray<>();

    protected void setProgressBarMinWidth(ProgressBar progressBar, int minWidth) {
        Field field = fieldCache.get(RR.styleable.ProgressBar_minWidth);
        try {
            if (field == null) {
                Field field1 = ProgressBar.class.getDeclaredField("mMinWidth");
                field1.setAccessible(true);
                fieldCache.put(RR.styleable.ProgressBar_minWidth, field1);
                field = field1;
            }
            field.set(progressBar, minWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    protected void setProgressBarMaxWidth(ProgressBar progressBar, int maxWidth) {
        Field field = fieldCache.get(RR.styleable.ProgressBar_maxWidth);
        try {
            if (field == null) {
                Field field1 = ProgressBar.class.getDeclaredField("mMaxWidth");
                field1.setAccessible(true);
                fieldCache.put(RR.styleable.ProgressBar_maxWidth, field1);
                field = field1;
            }
            field.set(progressBar, maxWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    protected void setProgressBarMinHeight(ProgressBar progressBar, int minHeight) {
        Field field = fieldCache.get(RR.styleable.ProgressBar_minHeight);
        try {
            if (field == null) {
                Field field1 = ProgressBar.class.getDeclaredField("mMinHeight");
                field1.setAccessible(true);
                fieldCache.put(RR.styleable.ProgressBar_minHeight, field1);
                field = field1;
            }
            field.set(progressBar, minHeight);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    protected void setProgressBarMaxHeight(ProgressBar progressBar, int maxHeight) {
        Field field = fieldCache.get(RR.styleable.ProgressBar_maxHeight);
        try {
            if (field == null) {
                Field field1 = ProgressBar.class.getDeclaredField("mMaxHeight");
                field1.setAccessible(true);
                fieldCache.put(RR.styleable.ProgressBar_maxHeight, field1);
                field = field1;
            }
            field.set(progressBar, maxHeight);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    protected void set_ProgressBar_mBehavior(ProgressBar progressBar, int value) {
        reflectSet(progressBar, "mBehavior", RR.styleable.ProgressBar_indeterminateBehavior, value);
    }
    protected void set_ProgressBar_mDuration(ProgressBar progressBar, int value) {
        reflectSet(progressBar, "mDuration", RR.styleable.ProgressBar_progressDrawable, value);
    }

    /*-- ExpandableListView --*/
    protected void set_ExpandableListView_indicatorLeft(ExpandableListView view, int value) {
        getViewMap(view).put(RR.styleable.ExpandableListView_indicatorLeft, value);
    }
    protected void set_ExpandableListView_indicatorRight(ExpandableListView view, int value) {
        getViewMap(view).put(RR.styleable.ExpandableListView_indicatorRight, value);
    }
    protected void set_ExpandableListView_childIndicatorLeft(ExpandableListView view, int value) {
        getViewMap(view).put(RR.styleable.ExpandableListView_childIndicatorLeft, value);
    }
    protected void set_ExpandableListView_childIndicatorRight(ExpandableListView view, int value) {
        getViewMap(view).put(RR.styleable.ExpandableListView_childIndicatorRight, value);
    }
    protected void set_ExpandableListView_groupIndicator(ExpandableListView view, Drawable groupIndicator) {
        getViewMap(view).put(RR.styleable.ExpandableListView_groupIndicator, groupIndicator);
        view.setGroupIndicator(groupIndicator);
    }
    private void handleExpandableListView(ExpandableListView view, SparseArray<Object> map) {
        int mIndicatorLeft = 0;
        Object value = map.valueAt(RR.styleable.ExpandableListView_indicatorLeft);
        if (value != null) mIndicatorLeft = (int) value;
        int mIndicatorRight = 0;
        value = map.valueAt(RR.styleable.ExpandableListView_indicatorRight);
        if (value != null) mIndicatorRight = (int) value;
        Drawable mGroupIndicator = null;
        value = map.valueAt(RR.styleable.ExpandableListView_groupIndicator);
        if (value != null) {
            mGroupIndicator = (Drawable) value;
        }
        if (mIndicatorRight == 0 && mGroupIndicator != null) {
            mIndicatorRight = mIndicatorLeft + mGroupIndicator.getIntrinsicWidth();
        }
        view.setIndicatorBounds(mIndicatorLeft, mIndicatorRight);

        int mChildIndicatorLeft = ExpandableListView.CHILD_INDICATOR_INHERIT;
        value = map.valueAt(RR.styleable.ExpandableListView_childIndicatorLeft);
        if (value != null) mChildIndicatorLeft = (int) value;
        int mChildIndicatorRight = ExpandableListView.CHILD_INDICATOR_INHERIT;
        value = map.valueAt(RR.styleable.ExpandableListView_childIndicatorRight);
        if (value != null) mChildIndicatorRight = (int) value;
        view.setChildIndicatorBounds(mChildIndicatorLeft, mChildIndicatorRight);
    }

    // todo: 支持Android之外的attr
    // ?attr

    /**
     * @param resId eg: android.R.attr.actionBarSize
     * @return
     */
    protected int getThemeDimension(int resId) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(resId, tv, true)) {
            if (tv.type == TYPE_DIMENSION) {
                return (int) tv.getDimension(metrics(context));
            } else if (tv.type == TYPE_REFERENCE) {
                return (int) context.getResources().getDimension(tv.data);
            }
        }

        // TODO: 2017/12/14
        return 0;
    }

    /**
     * todo：这种实现方式ok么？
     * @param resId attr id，比如android.R.attr.selectableItemBackground
     * @return Drawable
     */
    protected Drawable getThemeDrawable(int resId) {
        final TypedArray typedArray = context.obtainStyledAttributes(new int[]{resId});
        final Drawable drawable = typedArray.getDrawable(0);
        typedArray.recycle();

//        TypedValue tv = new TypedValue();
//        if (context.getTheme().resolveAttribute(resId, tv, true)) {
//            if (tv.type == TYPE_REFERENCE) {
//                return ContextCompat.getDrawable(context, tv.data);
//            }
//        }
        return drawable;
    }

    private static Method onFinishInflateMethod;

    protected void viewOnFinishInflate(View view) {
//        if (onFinishInflateMethod == null) {
//            try {
//                onFinishInflateMethod = View.class.getDeclaredMethod("onFinishInflate", View.class);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//                return;
//            }
//            onFinishInflateMethod.setAccessible(true);
//        }
//
//        try {
//            onFinishInflateMethod.invoke(view);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * An implementation of OnClickListener that attempts to lazily load a
     * named click handling method from a parent or ancestor context.
     */
    protected static class DeclaredOnClickListener implements View.OnClickListener {
        private final View mHostView;
        private final String mMethodName;

        private Method mResolvedMethod;
        private Context mResolvedContext;

        public DeclaredOnClickListener(@NonNull View hostView, @NonNull String methodName) {
            mHostView = hostView;
            mMethodName = methodName;
        }

        @Override
        public void onClick(@NonNull View v) {
            if (mResolvedMethod == null) {
                resolveMethod(mHostView.getContext(), mMethodName);
            }

            try {
                mResolvedMethod.invoke(mResolvedContext, v);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(
                        "Could not execute non-public method for android:onClick", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(
                        "Could not execute method for android:onClick", e);
            }
        }

        @NonNull
        private void resolveMethod(@Nullable Context context, @NonNull String name) {
            while (context != null) {
                try {
                    if (!context.isRestricted()) {
                        final Method method = context.getClass().getMethod(mMethodName, View.class);
                        if (method != null) {
                            mResolvedMethod = method;
                            mResolvedContext = context;
                            return;
                        }
                    }
                } catch (NoSuchMethodException e) {
                    // Failed to find method, keep searching up the hierarchy.
                }

                if (context instanceof ContextWrapper) {
                    context = ((ContextWrapper) context).getBaseContext();
                } else {
                    // Can't search up the hierarchy, null out and fail.
                    context = null;
                }
            }

            final int id = mHostView.getId();
            final String idText = id == View.NO_ID ? "" : " with id '"
                    + mHostView.getContext().getResources().getResourceEntryName(id) + "'";
            throw new IllegalStateException("Could not find method " + mMethodName
                    + "(View) in a parent or ancestor Context for android:onClick "
                    + "attribute defined on view " + mHostView.getClass() + idText);
        }
    }
}
