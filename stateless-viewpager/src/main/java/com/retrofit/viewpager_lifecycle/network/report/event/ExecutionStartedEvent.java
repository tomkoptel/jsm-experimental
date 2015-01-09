/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle.network.report.event;


import com.jaspersoft.android.retrofit.sdk.ojm.ReportExecutionResponse;

/**
 * Created by samsung on 1/5/15.
 */
public class ExecutionStartedEvent {
    private final ReportExecutionResponse reportExecutionResponse;

    public ExecutionStartedEvent(ReportExecutionResponse reportExecutionResponse) {
        this.reportExecutionResponse = reportExecutionResponse;
    }

    public ReportExecutionResponse response() {
        return reportExecutionResponse;
    }

    public static ExecutionStartedEvent forThe(ReportExecutionResponse response) {
        return new ExecutionStartedEvent(response);
    }

}
