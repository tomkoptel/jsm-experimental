/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.retrofit.viewpager_lifecycle.network.ApiErrorEvent;
import com.retrofit.viewpager_lifecycle.network.RestClient;
import com.retrofit.viewpager_lifecycle.network.report.ReportApi;
import com.retrofit.viewpager_lifecycle.network.report.event.ExecutionStartedEvent;
import com.retrofit.viewpager_lifecycle.network.report.event.ListParametersEvent;
import com.retrofit.viewpager_lifecycle.network.report.event.ParametersLoadedEvent;
import com.retrofit.viewpager_lifecycle.network.report.event.StartExecutionEvent;
import com.retrofit.viewpager_lifecycle.ojm.ExecutionRequest;
import com.retrofit.viewpager_lifecycle.ojm.InputControlsList;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.RestAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class JSActivity extends FragmentActivity {
    private static final String RESOURCE_URI = "/public/Samples/Reports/AllAccounts";
    private static final String RUNNING_EXTRA = "RUNNING_EXTRA";
    private static final String RUNNING_RESPONSE = "RUNNING_RESPONSE";

    private TextView mTextView;

    private final Bus mBus = EventBus.INSTANCE.bus();
    private boolean isRunning;
    private String mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.tag(JSActivity.class.getSimpleName());

        mTextView = (TextView) findViewById(android.R.id.text1);

        findViewById(R.id.selectPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomething();
            }
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestAdapter adapter = RestClient.BROKEN.adapter();
                ReportApi api = adapter.create(ReportApi.class);
                api.getInputControlsList(RESOURCE_URI)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Action1<InputControlsList>() {
                                    @Override
                                    public void call(InputControlsList controlsList) {

                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        mBus.post(ApiErrorEvent.valueOf(throwable));
                                    }
                                });
            }
        });

        if (savedInstanceState != null
                && savedInstanceState.containsKey(RUNNING_EXTRA)
                && savedInstanceState.containsKey(RUNNING_RESPONSE)
                ) {
            isRunning = savedInstanceState.getBoolean(RUNNING_EXTRA);
            mResponse = savedInstanceState.getString(RUNNING_RESPONSE);
        } else {
            mResponse = "hello world";
        }
        mTextView.setText(mResponse);

        applyProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RUNNING_EXTRA, isRunning);
        outState.putString(RUNNING_RESPONSE, mResponse);
    }

    private void doSomething() {
        setProgressEnabled(true);
        mBus.post(ListParametersEvent.forThe(RESOURCE_URI));
    }

    @Subscribe
    public void onParametersLoaded(ParametersLoadedEvent event) {
        applyProgress();
        ExecutionRequest configs = new ExecutionRequest()
                .withReportUnitUri(JSActivity.RESOURCE_URI)
                .withOutputFormat("html")
                .withAsync(true)
                .withInteractive(true)
                .withIgnorePagination(false);
        mBus.post(StartExecutionEvent.forThe(configs));
    }

    @Subscribe
    public void onExecutionStarted(ExecutionStartedEvent event) {
        setProgressEnabled(false);
        mResponse = event.response().toString();
        mTextView.setText(event.response().toString());
    }

    @Subscribe
    public void onApiError(ApiErrorEvent errorEvent) {
        setProgressEnabled(false);
    }

    private void setProgressEnabled(boolean enabled) {
        isRunning = enabled;
        applyProgress();
    }

    private void applyProgress() {
        setProgressBarIndeterminateVisibility(isRunning);
    }

}
