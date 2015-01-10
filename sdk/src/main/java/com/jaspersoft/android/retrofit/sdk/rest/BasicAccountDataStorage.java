/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.rest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class BasicAccountDataStorage implements AccountDataStorage {
    public static final String TAG = BasicAccountDataStorage.class.getSimpleName();

    private static final String COOKIE_KEY = "COOKIE_KEY";
    private final SharedPreferences mPreference;

    private BasicAccountDataStorage(Context context) {
        mPreference = context.getSharedPreferences(TAG, Activity.MODE_PRIVATE);
    }

    public static BasicAccountDataStorage get(Context context) {
        return new BasicAccountDataStorage(context);
    }

    public BasicAccountDataStorage withCookie(String cookie) {
        putCookie(cookie);
        return this;
    }

    @Override
    public void putCookie(String cookie) {
        if (TextUtils.isEmpty(cookie)) {
            throw new RuntimeException("Cookie value should not be empty");
        }
        mPreference.edit().putString("COOKIE_KEY", cookie).apply();
    }

    @Override
    public String getCookie() {
        return mPreference.getString(COOKIE_KEY, "");
    }
}
