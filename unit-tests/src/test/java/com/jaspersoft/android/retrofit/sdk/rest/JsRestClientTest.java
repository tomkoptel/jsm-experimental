/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.rest;

import com.google.gson.Gson;
import com.jaspersoft.android.retrofit.sdk.support.UnitTestSpecification;
import com.jaspersoft.android.retrofit.sdk.util.JasperSettings;

import org.junit.Before;
import org.junit.Test;

import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static com.jaspersoft.android.retrofit.sdk.database.JasperSdkDatabase.DEFAULT_ENDPOINT;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JsRestClientTest extends UnitTestSpecification {
    private JsRestClient restClient;
    private RestAdapter mockAdapter;
    private AccountService mockAccountService;

    private String mUrl;
    private GsonConverter mConverter;
    private MockRestAdapter mockRestAdapter;

    @Before
    public void setUp() {
        mUrl = DEFAULT_ENDPOINT + JasperSettings.DEFAULT_REST_VERSION;
        mConverter = new GsonConverter(new Gson());

        RestAdapter restAdapter = new RestAdapter.Builder() //
                .setEndpoint("http://example.com")
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .build();
        mockAdapter = spy(restAdapter);
        mockRestAdapter = MockRestAdapter.from(restAdapter);

        restClient = JsRestClient.basicBuilder(getContext())
                .setRestAdapter(mockAdapter)
                .build();

        mockAccountService = mockAdapter.create(AccountService.class);
        when(mockAdapter.create(AccountService.class)).thenReturn(mockAccountService);
    }

    @Test
    public void testAuthorizeMethodThrowsError() {
//        PublishSubject<Response> subject = PublishSubject.create();
//        when(mockAccountService.authorize(anyString()));
    }
}
