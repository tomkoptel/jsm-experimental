package com.retrofit.sample.authenticator.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.retrofit.sample.authenticator.HomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {
    private HomeActivity mActivity;

    public HomeActivityTest() {
        super(HomeActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testPreconditions() {
        assertNotNull(mActivity);
        assertNotNull(getInstrumentation());
    }
}
