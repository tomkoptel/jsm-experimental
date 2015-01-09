package com.jaspersoft.android.retrofit.sdk;

import android.accounts.AccountAuthenticatorActivity;
import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaspersoft.android.retrofit.sdk.database.table.JasperServerTable;
import com.jaspersoft.android.retrofit.sdk.provider.JasperSdkProvider;
import com.jaspersoft.android.retrofit.sdk.rest.AccountService;
import com.jaspersoft.android.retrofit.sdk.util.JasperSettings;

import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] FROM = {JasperServerTable.ALIAS};
    private static final int[] TO = {android.R.id.text1};
    private static final int LOAD_SERVERS = 0;

    public static String REST_VERSION_EXTRA = "AuthenticatorActivity.REST_VERSION_EXTRA";

    private SimpleCursorAdapter cursorAdapter;
    private RestAdapter restAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String restVersion = JasperSettings.DEFAULT_REST_VERSION;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(REST_VERSION_EXTRA)) {
            restVersion = extras.getString(REST_VERSION_EXTRA);
            if (TextUtils.isEmpty(restVersion)) {
                throw new RuntimeException("Rest version should not be empty");
            }
            // TODO: validate by reg expression
        }

        restAdapter = new RestAdapter.Builder()
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError cause) {
                        Toast.makeText(getApplicationContext(), cause.getMessage(), Toast.LENGTH_LONG).show();
                        return cause;
                    }
                })
                .setEndpoint(JasperSettings.DEFAULT_ENDPOINT + restVersion)
                .build();

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_login);
        }

        Spinner profiles = (Spinner) findViewById(R.id.profiles);
        profiles.setOnTouchListener( new View.OnTouchListener() {
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
        AccountService accountService = restAdapter.create(AccountService.class);
        String salt = "superuser:superuser";
        byte[] encodedSalt = Base64.encode(salt.getBytes(), Base64.NO_WRAP);
        accountService
                .authorize(new String(encodedSalt))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        Observable.just(response.getHeaders())
                        .filter(new Func1<List<Header>, Boolean>() {
                            @Override
                            public Boolean call(List<Header> headers) {
                                return null;
                            }
                        });
                    }
                });
    }

    public void authorizeAction(View view) {

    }
}
