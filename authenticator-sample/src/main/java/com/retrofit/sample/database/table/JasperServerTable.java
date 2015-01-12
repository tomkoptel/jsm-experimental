package com.retrofit.sample.database.table;

public interface JasperServerTable {
    String TABLE_NAME = "jasper_server";

    String _ID = "_id";

    String ALIAS = "alias";
    String SERVER_URL = "server_url";
    String ORGANIZATION = "organization";
    String VERSION_NAME = "version_name";
    String EDITION = "edition";
    String CREATED_AT = "created_at";
    String[] ALL_COLUMNS = new String[] {_ID, ALIAS, SERVER_URL, ORGANIZATION, VERSION_NAME, EDITION, CREATED_AT};

    String SQL_INSERT = "INSERT INTO jasper_server ( alias, server_url, version_name, edition ) VALUES ( ?, ?, ?, ? )";

    String SQL_CREATE = "CREATE TABLE jasper_server ( _id INTEGER PRIMARY KEY AUTOINCREMENT, alias TEXT NOT NULL UNIQUE, server_url TEXT NOT NULL, organization TEXT, version_name TEXT, edition TEXT, created_at DATETIME DEFAULT CURRENT_TIMESTAMP )";

    String SQL_DROP = "DROP TABLE IF EXISTS jasper_server";

    String WHERE_ID_EQUALS = _ID + "=?";

}