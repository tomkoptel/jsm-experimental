package com.retrofit.viewpager_lifecycle;

import android.app.Application;
import android.widget.Toast;

import com.retrofit.viewpager_lifecycle.network.ApiErrorEvent;
import com.retrofit.viewpager_lifecycle.network.RestClient;
import com.retrofit.viewpager_lifecycle.network.report.ReportApi;
import com.retrofit.viewpager_lifecycle.network.report.ReportExecutionService;
import com.retrofit.viewpager_lifecycle.report.CrashReportingTree;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class MyApplication extends Application {
    private ReportExecutionService reportExecutionService;
    private final Bus mBus = EventBus.INSTANCE.bus();

    @Override
    public void onCreate() {
        super.onCreate();

        ReportApi api = RestClient.INSTANCE.adapter().create(ReportApi.class);
        reportExecutionService = new ReportExecutionService(api, mBus);
        mBus.register(reportExecutionService);
        mBus.register(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    @Subscribe
    public void onApiError(ApiErrorEvent errorEvent) {
        Toast.makeText(this, errorEvent.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
