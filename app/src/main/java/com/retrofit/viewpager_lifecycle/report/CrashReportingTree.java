/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle.report;

import timber.log.Timber;

/**
 * A tree which logs important information for crash reporting.
 */
public class CrashReportingTree extends Timber.HollowTree {
    @Override
    public void i(String message, Object... args) {
        // TODO e.g., Crashlytics.log(String.format(message, args));
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        i(message, args); // Just add to the log.
    }

    @Override
    public void e(String message, Object... args) {
        i("ERROR: " + message, args); // Just add to the log.
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        e(message, args);

        // TODO e.g., Crashlytics.logException(t);
    }
}
