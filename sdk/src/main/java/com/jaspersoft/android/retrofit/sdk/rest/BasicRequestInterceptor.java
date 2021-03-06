/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.rest;

import retrofit.RequestInterceptor;

/**
 * Created by samsung on 1/10/15.
 */
public class BasicRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Content-Type", "application/json");
    }
}
