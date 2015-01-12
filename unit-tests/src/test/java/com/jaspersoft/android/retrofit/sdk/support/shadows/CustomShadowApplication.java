/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.support.shadows;

import android.app.Application;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowApplication;

import java.io.File;
import java.io.IOException;

/**
 * Just relocate the database file to a hard defined position.
 */
@Implements(Application.class)
public class CustomShadowApplication extends ShadowApplication {

    public static final String ALTERNATIVE_DB_PATH = "build/resources/unit-test.db";
    private File database = new File(ALTERNATIVE_DB_PATH);

    @Override
    @Implementation
    public File getDatabasePath(String name) {
        if (!database.exists()) {
            try {
                database.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return database;
    }

}