package com.retrofit.sample.account;

import android.accounts.AccountAuthenticatorActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import com.jaspersoft.android.retrofit.sdk.util.JasperSettings;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public static String REST_VERSION_EXTRA = "AuthenticatorActivity.REST_VERSION_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        String restVersion;
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(REST_VERSION_EXTRA)) {
            restVersion = extras.getString(REST_VERSION_EXTRA);
            if (TextUtils.isEmpty(restVersion)) {
                throw new RuntimeException("Rest version should not be empty");
            }
            // TODO: validate by reg expression
        } else {
            restVersion = JasperSettings.DEFAULT_REST_VERSION;
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, AuthenticatorFragment.getInstance(restVersion))
                    .commit();
        }
    }

}
