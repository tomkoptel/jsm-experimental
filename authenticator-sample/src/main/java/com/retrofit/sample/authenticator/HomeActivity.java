package com.retrofit.sample.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.jaspersoft.android.retrofit.sdk.rest.BasicAccountDataStorage;
import com.jaspersoft.android.retrofit.sdk.util.JasperAuthUtil;
import com.jaspersoft.android.retrofit.sdk.util.JasperSettings;


public class HomeActivity extends Activity {
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInOrCreateAnAccount();
        setContentView(R.layout.activity_home);
    }

    private void signInOrCreateAnAccount() {
        //Get list of accounts on device.
        AccountManager am = AccountManager.get(this);
        Account[] accountArray = am.getAccountsByType(JasperAuthUtil.JASPER_ACCOUNT_TYPE);
        if (accountArray.length == 0) {
            addAccount();
        } else {
            String authtoken = BasicAccountDataStorage.get(this).getServerCookie();
            Account account = accountArray[0];
            am.invalidateAuthToken(account.type, authtoken);
            am.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override
                public void run(AccountManagerFuture<Boolean> future) {
                    try {
                        Boolean result = future.getResult();
                        if (result) {
                            addAccount();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, mHandler);
        }
    }

    private void addAccount() {
        Intent intent = new Intent(JasperSettings.ACTION_AUTHORIZE);
        intent.putExtra("account_types", new String[]{"com.jaspersoft"});
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            // do something
        } else {
            finish();
        }
    }
}
