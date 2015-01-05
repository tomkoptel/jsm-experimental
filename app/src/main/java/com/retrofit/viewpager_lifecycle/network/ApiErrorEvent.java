/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle.network;

import android.text.TextUtils;

/**
 * Created by samsung on 1/5/15.
 */
public class ApiErrorEvent {
    private static final String DEFAULT_ERROR = "Ooops!";
    private final Throwable mThrowable;

    private ApiErrorEvent(Throwable throwable) {
        mThrowable = throwable;
    }

    public Throwable getCause() {
        return mThrowable.getCause();
    }

    public String getMessage() {
        if (TextUtils.isEmpty(mThrowable.getMessage())) {
            Throwable cause = mThrowable.getCause();
            if (cause != null) {
                return cause.toString();
            }
            return DEFAULT_ERROR;
        }
        return mThrowable.getMessage();
    }

    public static ApiErrorEvent valueOf(Throwable throwable) {
        return new ApiErrorEvent(throwable);
    }
}
