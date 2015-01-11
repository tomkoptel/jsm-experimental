/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaspersoft.android.retrofit.sdk.database.table.JasperServerTable;
import com.jaspersoft.android.retrofit.sdk.ojm.ServerInfo;
import com.jaspersoft.android.retrofit.sdk.provider.JasperSdkProvider;
import com.jaspersoft.android.retrofit.sdk.rest.BasicAccountDataStorage;
import com.jaspersoft.android.retrofit.sdk.rest.JsRestClient;
import com.jaspersoft.android.retrofit.sdk.rest.response.LoginResponse;
import com.jaspersoft.android.retrofit.sdk.util.JasperAuthUtil;
import com.jaspersoft.android.retrofit.sdk.util.JasperSettings;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscription;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

import static rx.android.app.AppObservable.bindFragment;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AuthenticatorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String DEFAULT_ENDPOINT = "http://mobiledemo.jaspersoft.com/jasperserver-pro";
    private static final String DEFAULT_ORGANIZATION = "organization_1";
    private static final String DEFAULT_USERNAME = "phoneuser";
    private static final String DEFAULT_PASSWORD = "phoneuser";

    private static final String[] FROM = {JasperServerTable.ALIAS};
    private static final int[] TO = {android.R.id.text1};

    private static final String FETCHING_KEY = "FETCHING";
    private static final String REST_VERSION = "REST_VERSION";

    private Spinner profiles;
    private Button tryDemo;
    private SimpleCursorAdapter cursorAdapter;
    private Subscription loginSubscription = Subscriptions.empty();
    private String mRestVersion;
    private boolean mFetching;

    private final ErrorHandler mErrorHandler = new ErrorHandler() {
        @Override
        public Throwable handleError(RetrofitError cause) {
            Toast.makeText(getActivity(), cause.getMessage(), Toast.LENGTH_LONG).show();
            return cause;
        }
    };
    private Observable<LoginResponse> tryDemoTask;

    public static AuthenticatorFragment getInstance(String restVersion) {
        AuthenticatorFragment authenticatorFragment = new AuthenticatorFragment();
        authenticatorFragment.setRetainInstance(true);
        Bundle args = new Bundle();
        args.putString(REST_VERSION, restVersion);
        authenticatorFragment.setArguments(args);
        return authenticatorFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mRestVersion = args.getString(REST_VERSION);
        }

        if (mRestVersion == null) {
            throw new IllegalStateException("Missing restVersion value");
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(FETCHING_KEY)){
            mFetching = savedInstanceState.getBoolean(FETCHING_KEY);
        }
        setProgressEnabled(mFetching);

        JsRestClient jsRestClient = JsRestClient.builder(getActivity())
                .setErrorHandler(mErrorHandler)
                .setEndpoint(DEFAULT_ENDPOINT + mRestVersion)
                .build();

        Observable<LoginResponse> demoLoginObservable =
                jsRestClient.login(DEFAULT_ORGANIZATION, DEFAULT_USERNAME, DEFAULT_PASSWORD);
        tryDemoTask = bindFragment(this, demoLoginObservable.cache());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_login, container, false);
        profiles = (Spinner) root.findViewById(R.id.profiles);
        tryDemo = (Button) root.findViewById(R.id.tryDemo);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        profiles.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startActivity(new Intent(JasperSettings.ACTION_LIST_SERVERS));
                }
                return true;
            }
        });

        cursorAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, null, FROM, TO, 0);
        profiles.setAdapter(cursorAdapter);

        getLoaderManager().initLoader(0, null, this);
        ViewObservable.clicks(tryDemo).subscribe(new Action1<OnClickEvent>() {
            @Override
            public void call(OnClickEvent onClickEvent) {
                tryDemoAction();
            }
        });

//        profiles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(),
//                        "On Item Select : \n" + parent.getItemAtPosition(position).toString(),
//                        Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), JasperSdkProvider.JASPER_SERVER_CONTENT_URI,
                JasperServerTable.ALL_COLUMNS, null, null, JasperServerTable.CREATED_AT + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (cursorAdapter != null) {
            cursorAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (cursorAdapter != null) {
            cursorAdapter.swapCursor(null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (tryDemoTask != null && mFetching) {
            makeLoginSubscription();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FETCHING_KEY, mFetching);
    }


    private void tryDemoAction() {
        makeLoginSubscription();
    }

    private void makeLoginSubscription() {
        final Context context = getActivity();
        setProgressEnabled(true);

        loginSubscription = tryDemoTask
                .subscribe(new Action1<LoginResponse>() {
                    @Override
                    public void call(LoginResponse response) {
                        applyAccountData(response);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                        setProgressEnabled(false);
                    }
                });
    }

    private void applyAccountData(LoginResponse response) {
        String cookie = response.getCookie();
        ServerInfo serverInfo = response.getServerInfo();
        BasicAccountDataStorage.get(getActivity())
                .putCookie(cookie)
                .putEdition(serverInfo.getEdition())
                .putVersionName(serverInfo.getVersion());

        Account account = new Account(DEFAULT_USERNAME, JasperAuthUtil.JASPER_ACCOUNT_TYPE);
        AccountManager accountManager = AccountManager.get(getActivity());
        accountManager.addAccountExplicitly(account, DEFAULT_PASSWORD, null);
        accountManager.setAuthToken(account, JasperAuthUtil.JASPER_AUTH_TOKEN_TYPE, cookie);

        Bundle data = new Bundle();
        data.putString(AccountManager.KEY_ACCOUNT_NAME, DEFAULT_USERNAME);
        data.putString(AccountManager.KEY_ACCOUNT_TYPE, JasperAuthUtil.JASPER_ACCOUNT_TYPE);
        data.putString(AccountManager.KEY_AUTHTOKEN, cookie);
        getAccountAuthenticatorActivity().setAccountAuthenticatorResult(data);

        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
        setProgressEnabled(false);

        Intent resultIntent = new Intent();
        resultIntent.putExtras(data);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        loginSubscription.unsubscribe();
        super.onDestroyView();
    }

    public void authorizeAction(View view) {

    }

    private void setProgressEnabled(boolean enabled) {
        mFetching = enabled;
        getActivity().setProgressBarIndeterminateVisibility(mFetching);
    }

    private AccountAuthenticatorActivity getAccountAuthenticatorActivity() {
        if (getActivity() instanceof AccountAuthenticatorActivity) {
            return (AccountAuthenticatorActivity) getActivity();
        } else {
            throw new IllegalStateException("Fragment can only be consumed " +
                    "within android.accounts.AccountAuthenticatorActivity");
        }
    }
}
