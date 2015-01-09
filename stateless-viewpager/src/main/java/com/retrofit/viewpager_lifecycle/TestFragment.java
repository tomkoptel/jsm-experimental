/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.github.stephanenicolas.loglifecycle.LogLifeCycle;

import java.util.Random;

@LogLifeCycle
public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    private static final String KEY_PAGE = "TestFragment:Page";
    private static final String KEY_COLOR = "TestFragment:Color";

    public static TestFragment newInstance(int page) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(KEY_CONTENT, "Page " + page);
        args.putInt(KEY_PAGE, page);
        fragment.setArguments(args);
        fragment.setRetainInstance(false);
        return fragment;
    }

    private String mContent = "???";
    private int mPage;
    private int mColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null)
                && savedInstanceState.containsKey(KEY_CONTENT)
                && savedInstanceState.containsKey(KEY_PAGE)
                ) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
            mPage = savedInstanceState.getInt(KEY_PAGE);
            mColor = savedInstanceState.getInt(KEY_COLOR);
        } else {
            Bundle args = getArguments();
            mContent = args.getString(KEY_CONTENT);
            mPage = args.getInt(KEY_PAGE);

            Random rnd = new Random();
            mColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView text = new TextView(getActivity());
        text.setGravity(Gravity.CENTER);
        text.setText(mContent);
        text.setTextSize(20 * getResources().getDisplayMetrics().density);
        text.setPadding(20, 20, 20, 20);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(text);

        layout.setBackgroundColor(mColor);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
        outState.putInt(KEY_PAGE, mPage);
        outState.putInt(KEY_COLOR, mColor);
    }
}