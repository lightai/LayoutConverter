package com.netease.layoutconverter.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.netease.layoutconverter.BaseLayout;
import com.netease.layoutconverter.Layouts;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

@Layouts(
        modulePackage = "com.netease.layoutconverter.demo",
        layouts = {"abs_list_view.xml",
                "activity_main.xml",
                "benchmark.xml",
                "dimen.xml",
                "expandable_list_view.xml",
                "group_view.xml",
                "image_view.xml",
                "include.xml",
                "linear_layout.xml",
                "list_view.xml",
                "progress_bar.xml",
                "relative_layout.xml",
                "view.xml"}
)
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private static final Class[] demoClasses = new Class[]{
            AbsListViewLayout.class,
            DimenLayout.class,
            ExpandableListViewLayout.class,
            GroupViewLayout.class,
            ImageViewLayout.class,
            IncludeLayout.class,
            LinearLayoutLayout.class,
            ListViewLayout.class,
            ProgressBarLayout.class,
            RelativeLayoutLayout.class,
            ViewLayout.class
    };

    DisplayMetrics dm;
    View listTV;
    ViewGroup rootLayout;
    ListPopupWindow popupWindow;

    View benchmarkLayout;
    TextView benchmarkResultTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivityMainLayout.from(this).inflate());

        dm = getResources().getDisplayMetrics();
        rootLayout = findViewById(R.id.root_layout);

        listTV = findViewById(R.id.list_tv);
        listTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    popupWindow.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        initPopupWindow();

        findViewById(R.id.benchmark_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                benchmark();
            }
        });

        benchmarkLayout = findViewById(R.id.benchmark_layout);
        benchmarkResultTextView = findViewById(R.id.benchmark_result_tv);

        findViewById(R.id.reset_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBenchmark();
            }
        });
    }

    private void benchmark() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        Trace.beginSection("LayoutInflater");
        View inflaterView = inflater.inflate(R.layout.benchmark, null);
        Trace.endSection();
        long t0 = Trace.sectionTimeMillis();

        Trace.beginSection("LayoutConverter");
        View layoutConverterView = BenchmarkLayout.from(this).inflate();
        Trace.endSection();
        long t1 = Trace.sectionTimeMillis();

        final String result = String.format(Locale.CHINA,
                "LayoutInflater time      : %d毫秒\n" +
                        "LayoutConverter time : %d毫秒", t0, t1);
        benchmarkResultTextView.setText(result);
    }

    private void showBenchmark() {
        rootLayout.removeAllViews();
        rootLayout.addView(benchmarkLayout);
    }

    private void initPopupWindow() {
        popupWindow = new ListPopupWindow(this);
        popupWindow.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return demoClasses.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Context context = parent.getContext();
                TextView textView;
                if (convertView == null) {
                    textView = new TextView(context);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    int padding = 15 * dm.densityDpi / 160;
                    textView.setPadding(padding, padding, padding, padding);
                    textView.setTextColor(Color.WHITE);
                    convertView = textView;
                }
                textView = (TextView) convertView;
                textView.setText(demoClasses[position].getSimpleName());
                return textView;
            }
        });
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class cl = demoClasses[position];
                rootLayout.removeAllViews();

                try {
                    Constructor constructor = cl.getDeclaredConstructor(Context.class);
                    constructor.setAccessible(true);
                    BaseLayout baseLayout = (BaseLayout) constructor.newInstance(view.getContext());
                    rootLayout.addView(baseLayout.inflate(parent, false));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

                popupWindow.dismiss();
            }
        });
        popupWindow.setContentWidth(200 * dm.densityDpi / 160);
        popupWindow.setHeight(dm.heightPixels - 100 * dm.densityDpi / 160);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        popupWindow.setAnchorView(listTV);
    }
}
