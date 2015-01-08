/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private TestPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mAdapter = new TestPagerAdapter(getSupportFragmentManager(), new FragmentCreator<Fragment, Integer>() {
            @Override
            public Fragment createFragment(Integer page) {
                return TestFragment.newInstance(page);
            }
        });
        mViewPager.setAdapter(mAdapter);

        if (savedInstanceState == null) {
            mAdapter.addPage();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.previousPage:
                showPreviousPage();
                return true;
            case R.id.nextPage:
                showNextPage();
                return true;
            case R.id.selectPage:
                selectPage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNextPage() {
        int index = mViewPager.getCurrentItem();
        int nextPage = index + 2;

        mAdapter.addPageOnDemand(nextPage);
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(index + 1);
    }

    private void showPreviousPage() {
        int currentPage = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(currentPage - 1);
    }

    private void selectPage() {
        NumberDialogFragment.OnPageSelectedListener onPageSelected =
                new NumberDialogFragment.OnPageSelectedListener() {
                    @Override
                    public void onPageSelected(int page) {
                        int count = mAdapter.getCount();
                        if (count < page) {
                            mAdapter.setCount(page);
                            mAdapter.notifyDataSetChanged();
                        }
                        mViewPager.setCurrentItem(page - 1);
                    }
                };
        int currentPage = mViewPager.getCurrentItem() + 1;
        int maxPage = mAdapter.getCount() + 10000;
        NumberDialogFragment.show(
                getSupportFragmentManager(), currentPage,
                maxPage, onPageSelected
        );
    }

}
