package com.jaspersoft.android.retrofit.sdk.rest;


import com.jaspersoft.android.retrofit.sdk.ojm.ServerInfo;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import rx.Observable;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface AccountService {
    @Headers({"Accept: application/repository.folder+json"})
    @GET("/resource")
    Observable<Response> authorize(@Header("Authorization") String authToken);
    @GET("/serverInfo")
    Observable<ServerInfo> getServerInfo(@Header("Set-cookie") String cookie);
}
