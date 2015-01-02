/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private TestPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.previous).setOnClickListener({ showPreviousPage() });
        findViewById(R.id.next).setOnClickListener({ showNextPage() });
        findViewById(R.id.selectPage).setOnClickListener({ selectPage() });

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mAdapter = new TestPagerAdapter(getSupportFragmentManager(), { page ->
            return TestFragment.newInstance(page);
        });
        mViewPager.setAdapter(mAdapter);

        if (savedInstanceState == null) {
            mAdapter.addPage();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showNextPage() {
        int index = mViewPager.getCurrentItem();
        int nextPage = index + 2;

        mAdapter.addPageOnDemand(nextPage);
        mViewPager.setCurrentItem(index + 1);
    }

    private void showPreviousPage() {
        int currentPage = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(currentPage - 1);
    }

    private void selectPage() {
        def onPageSelected = { page ->
            def count = mAdapter.getCount()
            if (count < page) {
                mAdapter.setCount(page)
                mAdapter.notifyDataSetChanged()
            }
            mViewPager.setCurrentItem(page - 1)
        }
        def currentPage = mViewPager.getCurrentItem() + 1
        def maxPage = mAdapter.getCount() + 1000
        NumberDialogFragment.show(
                getSupportFragmentManager(), currentPage,
                maxPage, onPageSelected
        )
    }

}
