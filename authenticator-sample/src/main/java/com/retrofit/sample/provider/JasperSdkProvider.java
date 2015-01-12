package com.retrofit.sample.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.retrofit.sample.database.JasperSdkDatabase;
import com.retrofit.sample.database.table.JasperServerTable;

import java.util.ArrayList;
import java.util.List;

public class JasperSdkProvider extends ContentProvider {

    public static final String AUTHORITY = "com.jaspersoft.android.retrofit.sdk.provider";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri JASPER_SERVER_CONTENT_URI = Uri.withAppendedPath(JasperSdkProvider.AUTHORITY_URI, JasperServerContent.CONTENT_PATH);


    private static final UriMatcher URI_MATCHER;
    protected JasperSdkDatabase mDatabase;

    private static final int JASPER_SERVER_DIR = 0;
    private static final int JASPER_SERVER_ID = 1;


    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, JasperServerContent.CONTENT_PATH, JASPER_SERVER_DIR);
        URI_MATCHER.addURI(AUTHORITY, JasperServerContent.CONTENT_PATH + "/#",    JASPER_SERVER_ID);

     }

    public static final class JasperServerContent implements BaseColumns {
        public static final String CONTENT_PATH = "jasper_server";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jaspersdk_database.jasper_server";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.jaspersdk_database.jasper_server";
    }


    @Override
    public boolean onCreate() {
        mDatabase = new JasperSdkDatabase(getContext());
        return true;
    }

    @Override
    public final String getType(final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case JASPER_SERVER_DIR:
                return JasperServerContent.CONTENT_TYPE;
            case JASPER_SERVER_ID:
                return JasperServerContent.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public final Cursor query(final Uri uri, String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final SQLiteDatabase dbConnection = mDatabase.getReadableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case JASPER_SERVER_ID:
                queryBuilder.appendWhere(JasperServerTable._ID + "=" + uri.getLastPathSegment());
            case JASPER_SERVER_DIR:
                queryBuilder.setTables(JasperServerTable.TABLE_NAME);
                break;

            default :
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }

        Cursor cursor = queryBuilder.query(dbConnection, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public final Uri insert(final Uri uri, final ContentValues values) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case JASPER_SERVER_DIR:
                case JASPER_SERVER_ID:
                    final long jasper_serverId = dbConnection.insertOrThrow(JasperServerTable.TABLE_NAME, null, values);
                    final Uri newJasperServerUri = ContentUris.withAppendedId(JASPER_SERVER_CONTENT_URI, jasper_serverId);
                    getContext().getContentResolver().notifyChange(newJasperServerUri, null);

                    dbConnection.setTransactionSuccessful();
                    return newJasperServerUri;
                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.endTransaction();
        }

        return null;
    }

    @Override
    public final int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int updateCount = 0;
        List<Uri> joinUris = new ArrayList<Uri>();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case JASPER_SERVER_DIR:
                    updateCount = dbConnection.update(JasperServerTable.TABLE_NAME, values, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case JASPER_SERVER_ID:
                   final long jasper_serverId = ContentUris.parseId(uri);
                   updateCount = dbConnection.update(JasperServerTable.TABLE_NAME, values,
                       JasperServerTable._ID + "=" + jasper_serverId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);

                   dbConnection.setTransactionSuccessful();
                   break;

                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            for (Uri joinUri : joinUris) {
                getContext().getContentResolver().notifyChange(joinUri, null);
            }
        }

        return updateCount;

    }

    @Override
    public final int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int deleteCount = 0;
        List<Uri> joinUris = new ArrayList<Uri>();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case JASPER_SERVER_DIR:
                    deleteCount = dbConnection.delete(JasperServerTable.TABLE_NAME, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case JASPER_SERVER_ID:
                    deleteCount = dbConnection.delete(JasperServerTable.TABLE_NAME, JasperServerTable.WHERE_ID_EQUALS, new String[] { uri.getLastPathSegment() });

                    dbConnection.setTransactionSuccessful();
                    break;

                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (deleteCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            for (Uri joinUri : joinUris) {
                getContext().getContentResolver().notifyChange(joinUri, null);
            }
        }

        return deleteCount;
    }
}