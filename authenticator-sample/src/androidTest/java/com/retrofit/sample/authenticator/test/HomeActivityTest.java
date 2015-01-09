package com.retrofit.sample.authenticator.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.retrofit.sample.authenticator.HomeActivity;
import com.retrofit.sample.authenticator.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jaspersoft.android.retrofit.sdk.R.id.tryDemo;

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

    @Test
    public void testTryDemo() {
        onView(withId(tryDemo)).perform(click());
        onView(withId(R.id.intro)).check(matches(withText(R.string.hello_world)));
    }

}
