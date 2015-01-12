/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.support;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jaspersoft.android.retrofit.sdk.support.shadows.CustomShadowApplication;

import org.junit.After;
import org.junit.Before;
import org.robolectric.Robolectric;

import java.io.File;

public class DatabaseSpecification extends UnitTestSpecification {

    private Context context;

    @Before
    public void initContext() {
        context = Robolectric.application;
        resetDatabase();
    }

    @After
    public void clearDatabase() {
        resetDatabase();
    }

    public void resetDatabase() {
        File dbFile = new File(CustomShadowApplication.ALTERNATIVE_DB_PATH);
        SQLiteDatabase.deleteDatabase(dbFile);
    }

    public Context getContext() {
        return context;
    }

    public ContentResolver getContentResolver() {
        return context.getContentResolver();
    }

    protected int columnIndex(Cursor cursor, String column) {
        return cursor.getColumnIndex(column);
    }
}