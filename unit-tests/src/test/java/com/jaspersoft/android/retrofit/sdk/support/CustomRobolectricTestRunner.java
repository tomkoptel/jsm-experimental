/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.support;

import android.os.Build;


import org.junit.runners.model.InitializationError;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.SdkConfig;
import org.robolectric.bytecode.ClassInfo;
import org.robolectric.bytecode.Setup;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class CustomRobolectricTestRunner extends RobolectricTestRunner {
    public CustomRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        // Pragma keys supported only on FROYO and higher
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class, "SDK_INT", 8);
    }

    @Override
    protected ClassLoader createRobolectricClassLoader(Setup setup, SdkConfig sdkConfig) {
        return super.createRobolectricClassLoader(new ExtraShadows(setup), sdkConfig);
    }

    class ExtraShadows extends Setup {
        private Setup setup;

        public ExtraShadows(Setup setup) {
            this.setup = setup;
        }

        public boolean shouldInstrument(ClassInfo classInfo) {
            boolean shoudInstrument = setup.shouldInstrument(classInfo);
            return shoudInstrument;
        }
    }
}
