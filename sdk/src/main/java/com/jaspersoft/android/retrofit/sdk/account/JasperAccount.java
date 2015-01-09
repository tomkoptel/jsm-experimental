package com.jaspersoft.android.retrofit.sdk.account;

import android.accounts.Account;
import android.text.TextUtils;

import com.jaspersoft.android.retrofit.sdk.model.JasperServer;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JasperAccount {
    private final JasperServer mServer;
    private final Account mAccount;

    public JasperAccount(Account account, JasperServer server) {
        this.mAccount = account;
        this.mServer = server;
    }

    public String generateJasperUsername() {
        if (TextUtils.isEmpty(mServer.getOrganization())) {
            return String.format("%s|%s", mServer.getOrganization(), mAccount.name);
        }
        return mAccount.name;
    }

//    public String createAuthHeader() {
//        String salt = generateJasperUsername() + ":" + accountPassword;
//        byte[] encodedSalt = Base64.encode(salt.getBytes(), Base64.NO_WRAP);
//        return new String(encodedSalt);
//    }
}
