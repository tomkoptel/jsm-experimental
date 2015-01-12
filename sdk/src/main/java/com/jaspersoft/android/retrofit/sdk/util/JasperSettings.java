package com.jaspersoft.android.retrofit.sdk.util;

/**
 * SDK constants
 *
 * @author Tom Koptel
 * @since 2.0
 */
public class JasperSettings {
    // Intent actions
    public static final String ACTION_AUTHORIZE = "jaspersoft.settings.AUTHORIZE_SETTINGS";
    public static final String ACTION_LIST_SERVERS = "jaspersoft.settings.LIST_SERVERS_SETTINGS";

    // REST constants
    public static final String DEFAULT_REST_VERSION = "/rest_v2";

    private JasperSettings() {
        throw new RuntimeException();
    }

}
