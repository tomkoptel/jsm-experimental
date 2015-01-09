package com.jaspersoft.android.retrofit.sdk.rest;


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
    @Headers({
            "Accept: application/repository.folder+json",
            "Content-Type: application/json"
    })
    @GET("/resource")
    Observable<Response> authorize(@Header("Authorization") String authToken);
}
