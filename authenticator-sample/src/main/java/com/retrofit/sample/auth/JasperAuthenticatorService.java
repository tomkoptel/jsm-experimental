package com.retrofit.sample.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JasperAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new JasperAuthenticator(this).getIBinder();
    }
}
