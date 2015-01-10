/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.rest;

import android.content.Context;

import com.jaspersoft.android.retrofit.sdk.ojm.ServerInfo;
import com.jaspersoft.android.retrofit.sdk.rest.response.LoginResponse;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit.Endpoint;
import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.Converter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JsRestClient {
    private final RestAdapter mAdapter;
    private final AccountDataStorage mPreferences;
    private AccessTokenEncoder mAccessTokenEncoder;

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public JsRestClient(RestAdapter adapter,
                        AccessTokenEncoder accessTokenEncoder,
                        AccountDataStorage accountPreferencesStorage) {
        mAdapter = adapter;
        mPreferences = accountPreferencesStorage;
        mAccessTokenEncoder = accessTokenEncoder;
    }

    public RestAdapter getRestAdapter() {
        return mAdapter;
    }

    public AccountDataStorage getAccountPreferences() {
        return mPreferences;
    }

    public void setAccessTokenEncoder(AccessTokenEncoder accessTokenEncoder) {
        mAccessTokenEncoder = accessTokenEncoder;
    }

    public Observable<LoginResponse> login() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (mAccessTokenEncoder == null) {
                    subscriber.onError(new RuntimeException("Set AccessTokenEncoder before method invocation"));
                }
                try {
                    subscriber.onNext(mAccessTokenEncoder.encodeToken());
                    subscriber.onCompleted();
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        });

        return login(observable);
    }

    public Observable<LoginResponse> login(final String organization, final String username, final String password) {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    BasicAccessTokenEncoder encoder = BasicAccessTokenEncoder.builder()
                            .setOrganization(organization)
                            .setUsername(username)
                            .setPassword(password)
                            .build();
                    subscriber.onNext(encoder.encodeToken());
                    subscriber.onCompleted();
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        });

        return login(observable);
    }

    public Observable<LoginResponse> login(Observable<String> tokenObservable) {
        final AccountService accountService = mAdapter.create(AccountService.class);
        return tokenObservable
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Response>>() {
                    @Override
                    public Observable<Response> call(String authorizationToken) {
                        return accountService.authorize(authorizationToken);
                    }
                })
                .map(new Func1<Response, List<Header>>() {
                    @Override
                    public List<Header> call(Response response) {
                        return response.getHeaders();
                    }
                })
                .flatMap(new Func1<List<Header>, Observable<Header>>() {
                    @Override
                    public Observable<Header> call(List<Header> headers) {
                        return Observable.from(headers);
                    }
                })
                .first(new Func1<Header, Boolean>() {
                    @Override
                    public Boolean call(Header header) {
                        return header.getName().equals("set-cookie");
                    }
                })
                .flatMap(new Func1<Header, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(final Header header) {
                        return Observable.zip(
                                Observable.just(header),
                                accountService.getServerInfo(header.getValue()),
                                new Func2<Header, ServerInfo, LoginResponse>() {
                                    @Override
                                    public LoginResponse call(Header header, ServerInfo serverInfo) {
                                        return new LoginResponse(header.getValue(), serverInfo);
                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static class Builder {
        private final Context context;
        private final RestAdapter.Builder adapterBuilder;

        private AccessTokenEncoder accessTokenEncoder;
        private AccountDataStorage accountDataStorage;
        private RequestInterceptor requestInterceptor;

        public Builder(Context context) {
            this.context = context;
            this.adapterBuilder = new RestAdapter.Builder();
        }

        public Builder setEndpoint(String endpoint) {
            adapterBuilder.setEndpoint(endpoint);
            return this;
        }

        public Builder setEndpoint(Endpoint endpoint) {
            adapterBuilder.setEndpoint(endpoint);
            return this;
        }

        public Builder setClient(final Client client) {
            adapterBuilder.setClient(client);
            return this;
        }

        public Builder setClient(Client.Provider clientProvider) {
            adapterBuilder.setClient(clientProvider);
            return this;
        }

        public Builder setExecutors(Executor httpExecutor, Executor callbackExecutor) {
            adapterBuilder.setExecutors(httpExecutor, callbackExecutor);
            return this;
        }

        public Builder setRequestInterceptor(RequestInterceptor requestInterceptor) {
            adapterBuilder.setRequestInterceptor(requestInterceptor);
            this.requestInterceptor = requestInterceptor;
            return this;
        }

        public Builder setConverter(Converter converter) {
            adapterBuilder.setConverter(converter);
            return this;
        }

        public Builder setProfiler(Profiler profiler) {
            adapterBuilder.setProfiler(profiler);
            return this;
        }

        public Builder setErrorHandler(ErrorHandler errorHandler) {
            adapterBuilder.setErrorHandler(errorHandler);
            return this;
        }

        public Builder setLog(RestAdapter.Log log) {
            adapterBuilder.setLog(log);
            return this;
        }

        public Builder setLogLevel(RestAdapter.LogLevel logLevel) {
            adapterBuilder.setLogLevel(logLevel);
            return this;
        }

        public Builder setAccessTokenEncoder(AccessTokenEncoder accessTokenEncoder) {
            this.accessTokenEncoder = accessTokenEncoder;
            return this;
        }

        public Builder setAccountDataStorage(AccountDataStorage accountDataStorage) {
            this.accountDataStorage = accountDataStorage;
            return this;
        }

        public JsRestClient build() {
            ensureSaneDefaults();
            return new JsRestClient(adapterBuilder.build(), accessTokenEncoder, accountDataStorage);
        }

        private void ensureSaneDefaults() {
            if (accountDataStorage == null) {
                setAccountDataStorage(BasicAccountDataStorage.get(context));
            }
            if (requestInterceptor == null) {
                setRequestInterceptor(new BasicRequestInterceptor());
            }
        }
    }
}
