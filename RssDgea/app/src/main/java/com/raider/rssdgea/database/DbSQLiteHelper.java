package com.raider.rssdgea.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Raider on 18/04/16.
 */
public class DbSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE rssitems (title TEXT, category TEXT, " +
            "description TEXT, url TEXT, guid TEXT, pubDate TIMESTAMP, author TEXT)";

    public DbSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sq) {
        sq.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sq, int i, int i1) {}
}
