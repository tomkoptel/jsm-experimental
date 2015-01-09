package com.jaspersoft.android.retrofit.sdk.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.jaspersoft.android.retrofit.sdk.database.table.JasperServerTable;

import java.util.ArrayList;
import java.util.List;

public class JasperServer implements Parcelable {
    private long mRowId;
    private String mAlias;
    private String mServerUrl;
    private String mOrganization;
    private String mVersionName;
    private String mEdition;
    private String mCreatedAt;


    private ContentValues mValues = new ContentValues();

    public JasperServer() {
    }

    public JasperServer(final Cursor cursor) {
        this(cursor, false);
    }

    public JasperServer(final Cursor cursor, boolean prependTableName) {
        String prefix = prependTableName ? JasperServerTable.TABLE_NAME + "_" : "";
        setRowId(cursor.getLong(cursor.getColumnIndex(prefix + JasperServerTable._ID)));
        setAlias(cursor.getString(cursor.getColumnIndex(prefix + JasperServerTable.ALIAS)));
        setServerUrl(cursor.getString(cursor.getColumnIndex(prefix + JasperServerTable.SERVER_URL)));
        setOrganization(cursor.getString(cursor.getColumnIndex(prefix + JasperServerTable.ORGANIZATION)));
        setVersionName(cursor.getString(cursor.getColumnIndex(prefix + JasperServerTable.VERSION_NAME)));
        setEdition(cursor.getString(cursor.getColumnIndex(prefix + JasperServerTable.EDITION)));
        setCreatedAt(cursor.getString(cursor.getColumnIndex(prefix + JasperServerTable.CREATED_AT)));
    }

    public ContentValues getContentValues() {
        return mValues;
    }

    public Long getRowId() {
        return mRowId;
    }

    public void setRowId(long _id) {
        mRowId = _id;
        mValues.put(JasperServerTable._ID, _id);
    }

    public void setAlias(String alias) {
        mAlias = alias;
        mValues.put(JasperServerTable.ALIAS, alias);
    }

    public String getAlias() {
        return mAlias;
    }


    public void setServerUrl(String server_url) {
        mServerUrl = server_url;
        mValues.put(JasperServerTable.SERVER_URL, server_url);
    }

    public String getServerUrl() {
        return mServerUrl;
    }


    public void setOrganization(String organization) {
        mOrganization = organization;
        mValues.put(JasperServerTable.ORGANIZATION, organization);
    }

    public String getOrganization() {
        return mOrganization;
    }


    public void setVersionName(String version_name) {
        mVersionName = version_name;
        mValues.put(JasperServerTable.VERSION_NAME, version_name);
    }

    public String getVersionName() {
        return mVersionName;
    }


    public void setEdition(String edition) {
        mEdition = edition;
        mValues.put(JasperServerTable.EDITION, edition);
    }

    public String getEdition() {
        return mEdition;
    }


    public void setCreatedAt(String created_at) {
        mCreatedAt = created_at;
        mValues.put(JasperServerTable.CREATED_AT, created_at);
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public JasperServer withRowId(long _id) {
        setRowId(_id);
        return this;
    }

    public JasperServer withAlias(String alias) {
        setAlias(alias);
        return this;
    }

    public JasperServer withServerUrl(String server_url) {
        setServerUrl(server_url);
        return this;
    }

    public JasperServer withOrganization(String organization) {
        setOrganization(organization);
        return this;
    }

    public JasperServer withVersionName(String version_name) {
        setVersionName(version_name);
        return this;
    }

    public JasperServer withEdition(String edition) {
        setEdition(edition);
        return this;
    }

    public JasperServer withCreatedAt(String created_at) {
        setCreatedAt(created_at);
        return this;
    }

    public static List<JasperServer> listFromCursor(Cursor cursor) {
        List<JasperServer> list = new ArrayList<JasperServer>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new JasperServer(cursor));
            } while (cursor.moveToNext());
        }

        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mRowId);
        dest.writeString(this.mAlias);
        dest.writeString(this.mServerUrl);
        dest.writeString(this.mOrganization);
        dest.writeString(this.mVersionName);
        dest.writeString(this.mEdition);
        dest.writeString(this.mCreatedAt);
    }

    public JasperServer(Parcel in) {
        setRowId(in.readLong());
        setAlias(in.readString());
        setServerUrl(in.readString());
        setOrganization(in.readString());
        setVersionName(in.readString());
        setEdition(in.readString());
        setCreatedAt(in.readString());
    }

    public static final Creator<JasperServer> CREATOR = new Creator<JasperServer>() {
        public JasperServer createFromParcel(Parcel source) {
            return new JasperServer(source);
        }

        public JasperServer[] newArray(int size) {
            return new JasperServer[size];
        }
    };

}