package com.jaspersoft.android.retrofit.sdk;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
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
import rx.functions.Action1;

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] FROM = {JasperServerTable.ALIAS};
    private static final int[] TO = {android.R.id.text1};
    private static final int LOAD_SERVERS = 0;

    public static String REST_VERSION_EXTRA = "AuthenticatorActivity.REST_VERSION_EXTRA";

    private String mRestVersion;
    private SimpleCursorAdapter cursorAdapter;

    private final ErrorHandler mErrorHandler = new ErrorHandler() {
        @Override
        public Throwable handleError(RetrofitError cause) {
            Toast.makeText(getApplicationContext(), cause.getMessage(), Toast.LENGTH_LONG).show();
            return cause;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(REST_VERSION_EXTRA)) {
            mRestVersion = extras.getString(REST_VERSION_EXTRA);
            if (TextUtils.isEmpty(mRestVersion)) {
                throw new RuntimeException("Rest version should not be empty");
            }
            // TODO: validate by reg expression
        } else {
            mRestVersion = JasperSettings.DEFAULT_REST_VERSION;
        }

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_login);
        }

        Spinner profiles = (Spinner) findViewById(R.id.profiles);
        profiles.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startActivity(new Intent(JasperSettings.ACTION_LIST_SERVERS));
                }
                return true;
            }
        });

        cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null, FROM, TO, 0);
        profiles.setAdapter(cursorAdapter);

        getLoaderManager().initLoader(LOAD_SERVERS, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, JasperSdkProvider.JASPER_SERVER_CONTENT_URI,
                JasperServerTable.ALL_COLUMNS, null, null, JasperServerTable.CREATED_AT + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        if (loader.getId() == LOAD_SERVERS) {
            if (cursorAdapter != null) {
                cursorAdapter.swapCursor(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == LOAD_SERVERS) {
            if (cursorAdapter != null) {
                cursorAdapter.swapCursor(null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release reference
        cursorAdapter = null;
    }

    public void tryDemoAction(View view) {
        String organization = "organization_1";
        final String username = "phoneuser";
        final String password = "phoneuser";
        final Context context = this;

        JsRestClient jsRestClient = JsRestClient.builder(this)
                .setErrorHandler(mErrorHandler)
                .setEndpoint(JasperSettings.DEFAULT_ENDPOINT + mRestVersion)
                .build();

        jsRestClient
                .login(organization, username, password)
                .subscribe(new Action1<LoginResponse>() {
                    @Override
                    public void call(LoginResponse response) {
                        String cookie = response.getCookie();
                        ServerInfo serverInfo = response.getServerInfo();
                        BasicAccountDataStorage.get(context)
                                .putCookie(cookie)
                                .putEdition(serverInfo.getEdition())
                                .putVersionName(serverInfo.getVersion());

                        Account account = new Account(username, JasperAuthUtil.JASPER_ACCOUNT_TYPE);
                        AccountManager accountManager = AccountManager.get(context);
                        accountManager.addAccountExplicitly(account, password, null);
                        accountManager.setAuthToken(account, null, cookie);
                    }
                });
    }

    public void authorizeAction(View view) {

    }
}
