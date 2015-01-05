/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.retrofit.viewpager_lifecycle.network;

import android.util.Base64;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by samsung on 1/5/15.
 */
public enum RestClient {
    INSTANCE("http://build-master.jaspersoft.com:5980/jrs-pro-trunk"),
    BROKEN("http://172.17.10.71:8080/jasperserver-pro-561");

    private final RestAdapter mRestAdapter;

    RestClient(String endpoint) {
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
        mRestAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(requestInterceptor)
                .setEndpoint(endpoint)
                .build();
    }

    public RestAdapter adapter() {
        return mRestAdapter;
    }
}
