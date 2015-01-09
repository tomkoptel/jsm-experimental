package com.retrofit.sample.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jaspersoft.android.retrofit.sdk.util.JasperAuthUtil;
import com.jaspersoft.android.retrofit.sdk.util.JasperSettings;


public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signInOrCreateAnAccount();
    }

    private void signInOrCreateAnAccount() {
        //Get list of accounts on device.
        AccountManager am = AccountManager.get(this);
        Account[] accountArray = am.getAccountsByType(JasperAuthUtil.JASPER_ACCOUNT_TYPE);
        if (accountArray.length == 0) {
            //Send the user to the "Add Account" page.
            Intent intent = new Intent(JasperSettings.ACTION_AUTHORIZE);
            intent.putExtra("account_types", new String[] {"com.jaspersoft"});
            startActivity(intent);
        } else {
            //Try to log the user in with the first account on the device.
        }
    }

}
