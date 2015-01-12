package com.retrofit.sample.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.retrofit.sample.database.table.JasperServerTable;
import com.retrofit.sample.model.JasperServer;

public class JasperSdkDatabase extends SQLiteOpenHelper {
    public static final String DEFAULT_ENDPOINT = "http://mobiledemo.jaspersoft.com/jasperserver-pro";
    public static final String DEFAULT_ORGANIZATION = "organization_1";

    public static final boolean FOREIGN_KEYS_SUPPORTED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    private static final String DATABASE_NAME = "jasper_sdk.db";
    private static final int DATABASE_VERSION = 1;

    public JasperSdkDatabase(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        executePragmas(db);
        db.execSQL(JasperServerTable.SQL_CREATE);
        insertDefaultServer(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        executePragmas(db);
        switch (oldVersion) {
            case 1:
                db.execSQL("CREATE TABLE jasper_server ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "alias TEXT NOT NULL UNIQUE, server_url TEXT NOT NULL, organization TEXT, " +
                        "version_name TEXT, edition TEXT, created_at DATETIME DEFAULT CURRENT_TIMESTAMP )");
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            executePragmas(db);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS
    //////////////////////////////////////////////////////////////////////////////////////

    protected void executePragmas(SQLiteDatabase db) {
        if (FOREIGN_KEYS_SUPPORTED) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    private void insertDefaultServer(SQLiteDatabase db) {
        JasperServer jasperServer = new JasperServer()
                .withAlias("Jasper Mobile")
                .withOrganization(DEFAULT_ORGANIZATION)
                .withServerUrl(DEFAULT_ENDPOINT);
        db.insert(JasperServerTable.TABLE_NAME, null, jasperServer.getContentValues());
    }
}