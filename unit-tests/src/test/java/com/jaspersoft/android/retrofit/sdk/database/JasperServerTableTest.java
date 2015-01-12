/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.retrofit.sdk.database;

import android.net.Uri;

import com.jaspersoft.android.retrofit.sdk.model.JasperServer;
import com.jaspersoft.android.retrofit.sdk.provider.JasperSdkProvider;
import com.jaspersoft.android.retrofit.sdk.support.DatabaseSpecification;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JasperServerTableTest extends DatabaseSpecification {

    @Test
    public void aliasShouldBeUnique() {
        JasperServer server = new JasperServer()
                .withAlias("Alias")
                .withServerUrl("http://some.url");
        JasperServer duplicateServer = new JasperServer()
                .withAlias("Alias")
                .withServerUrl("http://some.url");
        Uri newUri = getContentResolver()
                .insert(JasperSdkProvider.JASPER_SERVER_CONTENT_URI, server.getContentValues());
        assertNewUri(newUri);

        Uri uriOfDuplicate = getContentResolver()
                .insert(JasperSdkProvider.JASPER_SERVER_CONTENT_URI, duplicateServer.getContentValues());
        assertThat(uriOfDuplicate, nullValue());
    }

    @Test
    public void aliasShouldNotBeNull() {
        JasperServer server = new JasperServer()
                .withAlias(null)
                .withServerUrl("http://some.url");
        Uri newUri = getContentResolver()
                .insert(JasperSdkProvider.JASPER_SERVER_CONTENT_URI, server.getContentValues());
        assertThat(newUri, nullValue());
    }

    @Test
    public void serverUrlShouldNotBeNull() {
        JasperServer server = new JasperServer()
                .withAlias("Alias")
                .withServerUrl(null);
        Uri newUri = getContentResolver()
                .insert(JasperSdkProvider.JASPER_SERVER_CONTENT_URI, server.getContentValues());
        assertThat(newUri, nullValue());
    }

    private void assertNewUri(Uri uri) {
        assertThat(uri, notNullValue());
        assertThat(getIdFromUri(uri), greaterThan(0L));
    }

    private long getIdFromUri(Uri uri) {
        return Long.valueOf(uri.getLastPathSegment());
    }

}
