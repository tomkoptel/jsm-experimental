/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle.network.report;

import com.jaspersoft.android.retrofit.sdk.ojm.ReportParametersList;
import com.retrofit.viewpager_lifecycle.network.ApiErrorEvent;
import com.retrofit.viewpager_lifecycle.network.report.event.ExecutionStartedEvent;
import com.retrofit.viewpager_lifecycle.network.report.event.ListParametersEvent;
import com.retrofit.viewpager_lifecycle.network.report.event.ParametersLoadedEvent;
import com.retrofit.viewpager_lifecycle.network.report.event.StartExecutionEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by samsung on 1/5/15.
 */
public class ReportExecutionService {
    private final ReportApi mApi;
    private final Bus mBus;
    private final Action1<Throwable> mErrorAction;

    public ReportExecutionService(ReportApi api, Bus bus) {
        mApi = api;
        mBus = bus;
        mErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mBus.post(ApiErrorEvent.valueOf(throwable));
            }
        };
    }

    @Subscribe
    public void listReportParameters(ListParametersEvent event) {
        mApi.postInputControlsList(event.uri(), new ReportParametersList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<InputControlsList>() {
                            @Override
                            public void call(InputControlsList response) {
                                mBus.post(ParametersLoadedEvent.forThe(response));
                            }
                        }, mErrorAction
                );

    }

    @Subscribe
    public void startReportExecution(StartExecutionEvent event) {
        mApi.postReportExecution(event.configs())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<ReportExecutionResponse>() {
                            @Override
                            public void call(ReportExecutionResponse response) {
                                mBus.post(ExecutionStartedEvent.forThe(response));
                            }
                        }, mErrorAction
                );
    }
}
