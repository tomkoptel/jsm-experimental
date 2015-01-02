/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle.api;

import com.retrofit.viewpager_lifecycle.ojm.ExecutionRequest;
import com.retrofit.viewpager_lifecycle.ojm.InputControlsList;
import com.retrofit.viewpager_lifecycle.ojm.ReportExecutionResponse;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by samsung on 1/2/15.
 */
public interface ReportExecutionService {
    @GET("/rest_v2/reports{uri}/inputControls")
    Observable<InputControlsList> getInputControlsList(
            @Path(value = "uri", encode = false) String resourceUri
    );
    @POST("/rest_v2/reportExecutions")
    Observable<ReportExecutionResponse> reportExecution(
            @Body ExecutionRequest request
    );
}
