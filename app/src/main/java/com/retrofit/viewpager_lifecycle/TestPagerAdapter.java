/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 1/2/15.
 */
public class TestPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<Integer> pages = new ArrayList<>();
    private int mCount;

    public TestPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public boolean containsPage(int page) {
        return pages.contains(page);
    }

    public void addPageOnDemand(int page) {
        if (!containsPage(page)) {
            addPage();
        }
    }

    public void addPage() {
        mCount++;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        int page = position + 1;
        TestFragment fragment = TestFragment.newInstance(page);
        pages.add(page);
        fragments.add(fragment);
        return fragment;
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    @Override
    public int getItemPosition(Object object) {
            /*
             * Purpose of this method is to check whether an item in the adapter
             * still exists in the dataset and where it should show.
             * For each entry in dataset, request its Fragment.
             *
             * If the Fragment is found, return its (new) position. There's
             * no need to return POSITION_UNCHANGED; ViewPager handles it.
             *
             * If the Fragment passed to this method is not found, remove all
             * references and let the ViewPager remove it from display by
             * by returning POSITION_NONE;
             */
        TestFragment fragment = (TestFragment) object;
        if (fragments.contains(fragment)) {
            return fragments.indexOf(fragment);
        }

        // if we arrive here, the data-item for which the Fragment was created
        // does not exist anymore.
        // Let ViewPager remove the Fragment by returning POSITION_NONE.
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Parcelable saveState() {
        Bundle state = new Bundle();
        state.putInt("COUNT", mCount);
        return state;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            mCount = bundle.getInt("COUNT");
            notifyDataSetChanged();
        }
    }
}