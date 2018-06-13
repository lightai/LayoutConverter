package com.netease.layoutconverter.demo;

import android.util.Log;

import java.util.ArrayDeque;

/**
 * 代码执行时间监控
 * Created by hzwangpeng2015 on 2017/1/13.
 */
public class Trace {
    public static final String TAG = "Trace";

    private static ThreadLocal<ArrayDeque<Long>> timeThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<ArrayDeque<String>> nameThreadLocal = new ThreadLocal<>();

    private static long sectionTimeMillis;
    /**
     * care must be taken to ensure that:
     * beginSection / endSection pairs are properly nested and called from the same
     * thread.
     */
    public static void beginSection(String sectionName) {
        ArrayDeque<Long> timeStack = timeThreadLocal.get();
        if (timeStack == null) {
            timeStack = new ArrayDeque<>();
            timeThreadLocal.set(timeStack);
        }

        ArrayDeque<String> nameStack = nameThreadLocal.get();
        if (nameStack == null) {
            nameStack = new ArrayDeque<>();
            nameThreadLocal.set(nameStack);
        }

        sectionName = "Trace." + sectionName;
        timeStack.push(System.nanoTime());
        nameStack.push(sectionName);
    }

    /**
     * Writes a trace message to indicate that a given section of code has ended. This call must
     * be preceeded by a corresponding call to {@link #beginSection(String)}. Calling this method
     * will mark the end of the most recently begun section of code, so care must be taken to
     * ensure that beginSection / endSection pairs are properly nested and called from the same
     * thread.
     */
    public static void endSection() {
        ArrayDeque<Long> timeStack = timeThreadLocal.get();
        ArrayDeque<String> nameStack = nameThreadLocal.get();
        long ms = (System.nanoTime() - timeStack.pop()) / 1000_000;
        Log.i(nameStack.pop(), "spend:" + ms + "ms");
        sectionTimeMillis = ms;
    }

    public static long sectionTimeMillis() {
        return sectionTimeMillis;
    }
}
