package com.jaspersoft.android.retrofit.sdk;

import android.accounts.AccountAuthenticatorActivity;
import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.jaspersoft.android.retrofit.sdk.database.table.JasperServerTable;
import com.jaspersoft.android.retrofit.sdk.provider.JasperSdkProvider;
import com.jaspersoft.android.retrofit.sdk.util.JasperSettings;

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] FROM = {JasperServerTable.ALIAS};
    private static final int[] TO = {android.R.id.text1};
    private static final int LOAD_SERVERS = 0;

    private SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    }

    public void authorizeAction(View view) {

    }
}
