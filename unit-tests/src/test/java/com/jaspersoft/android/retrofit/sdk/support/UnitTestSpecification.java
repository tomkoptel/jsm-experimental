/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.support;

import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowLog;

import java.io.IOException;
import java.util.logging.LogManager;

@RunWith(CustomRobolectricTestRunner.class)
public abstract class UnitTestSpecification {

    public UnitTestSpecification() {
        // found no other way to set LogManager configuration by property file
        try {
            LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/logging.properties"));
            ShadowLog.stream = System.out; // current ShadowLog ignore log levels
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
