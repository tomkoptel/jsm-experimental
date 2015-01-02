/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Base64
import android.widget.Toast
import com.retrofit.viewpager_lifecycle.api.ReportExecutionService
import com.retrofit.viewpager_lifecycle.ojm.ExecutionRequest
import retrofit.RequestInterceptor
import retrofit.RestAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

public class JSActivity extends FragmentActivity {
    private static final String ENDPOINT = "http://mobiledemo.jaspersoft.com/jasperserver-pro";
    private static final String RESOURCE_URI = "/Reports/AllAccounts";

    private ReportExecutionService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        findViewById(R.id.selectPage).setOnClickListener({ doSomething() })
        setupApiService()
    }

    def doSomething() {
        ExecutionRequest executionData = new ExecutionRequest()
                .withBaseUrl(ENDPOINT)
                .withReportUnitUri(RESOURCE_URI)
                .withOutputFormat("html")
                .withAsync(true)
                .withInteractive(true)
                .withIgnorePagination(false)
                .withInteractive(true)

        final Context context = this
        mService.getInputControlsList(RESOURCE_URI)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap({ response -> mService.reportExecution(executionData) })
                .subscribe(
                { executionResponse ->
                    Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show()
                },
                { Throwable exception ->
                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show()
                })
    }

    def setupApiService() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                String authorisation = "superuser:superuser"
                byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes(), Base64.NO_WRAP)
                String authToken = "Basic " + new String(encodedAuthorisation)

                request.addHeader("Authorization", authToken);
                request.addHeader("Accept", "application/json");
                request.addHeader("Content-type", "application/json");
            }
        }
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(ENDPOINT)
                .build()
        mService = restAdapter.create(ReportExecutionService.class)
    }

}
