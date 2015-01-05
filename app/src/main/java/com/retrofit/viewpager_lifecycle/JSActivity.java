/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.retrofit.viewpager_lifecycle.api.ReportExecutionService;
import com.retrofit.viewpager_lifecycle.ojm.ExecutionRequest;
import com.retrofit.viewpager_lifecycle.ojm.InputControlsList;
import com.retrofit.viewpager_lifecycle.ojm.ReportExecutionResponse;
import com.retrofit.viewpager_lifecycle.ojm.ReportParametersList;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class JSActivity extends FragmentActivity {
    private static final String ENDPOINT = "http://mobiledemo.jaspersoft.com/jasperserver-pro";
    private static final String RESOURCE_URI = "/public/Samples/Reports/AllAccounts";

    private ReportExecutionService mService;
    private TextView mTextView;

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.tag(JSActivity.class.getSimpleName());
        setupApiService();

        mTextView = (TextView) findViewById(android.R.id.text1);
        mTextView.setText("hello world");

        findViewById(R.id.selectPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomething();
            }
        });
    }

    private void doSomething() {
        final Context context = this;
        Observable<InputControlsList> listInputControls =
                mService.postInputControlsList(RESOURCE_URI, new ReportParametersList());

        Subscription subscription = listInputControls
                .flatMap(new Func1<InputControlsList, Observable<ReportExecutionResponse>>() {
                    @Override
                    public Observable<ReportExecutionResponse> call(InputControlsList inputControlsList) {
                        return mService.postReportExecution(createExecutionData());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<ReportExecutionResponse>() {
                            @Override
                            public void call(ReportExecutionResponse response) {
                                mTextView.setText(response.toString());
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Timber.e(throwable.getMessage(), throwable);
                                Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        mCompositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }

    private ExecutionRequest createExecutionData() {
        return new ExecutionRequest()
                .withReportUnitUri(JSActivity.RESOURCE_URI)
                .withOutputFormat("html")
                .withAsync(true)
                .withInteractive(true)
                .withIgnorePagination(false);
    }

    private void setupApiService() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                String authorisation = "superuser:superuser";
                byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes(), Base64.NO_WRAP);
                String authToken = "Basic " + new String(encodedAuthorisation);

                request.addHeader("Authorization", authToken);
                request.addHeader("Accept", "application/json");
                request.addHeader("Content-type", "application/json");
            }
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(ENDPOINT)
                .build();
        mService = restAdapter.create(ReportExecutionService.class);
    }

}
