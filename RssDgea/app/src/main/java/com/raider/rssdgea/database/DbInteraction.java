package com.raider.rssdgea.database;

/**
 * Created by Raider on 18/04/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.raider.rssdgea.dataTemplates.RssItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbInteraction {

    private DbSQLiteHelper dbSQLiteHelper;

    public DbInteraction(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this.dbSQLiteHelper = new DbSQLiteHelper(context,name,factory,version);
    }

    private SQLiteDatabase getReadableDb() {
        return dbSQLiteHelper.getReadableDatabase();
    }

    private SQLiteDatabase getWritableDb() {
        return dbSQLiteHelper.getWritableDatabase();
    }

    public void saveItems(List<RssItem> items) {
        SQLiteDatabase db = getWritableDb();
        if (db != null) {
            for (RssItem r: items) {
              if (checkItem(r).equals(false)) {
                  try {
                      insertInto(r);
                  } catch (ParseException e) {
                      e.printStackTrace();
                  }
              }
            }
        }

        deleteLastEntries();

        if (db != null) db.close();
    }

    private Boolean checkItem(RssItem r) {

        SQLiteDatabase db = getReadableDb();

        String[] args = {r.getTitle(),r.getUrl()};
        Cursor c = db.rawQuery("SELECT * FROM rssitems WHERE title = ? AND url = ?", args);

        if(c.moveToFirst()){
            if (db != null) db.close();
            return true;
        }

        if (db != null) db.close();
        return false;
    }

    private void insertInto(RssItem r) throws ParseException {

        SQLiteDatabase db = getWritableDb();

        ContentValues reg = new ContentValues();
        reg.put("title", r.getTitle());
        reg.put("category", r.getFirstCategory());
        reg.put("description", r.getDescription());
        reg.put("url", r.getUrl());
        reg.put("guid", r.getGuid());
        reg.put("pubDate", formatDate(r.getPubDate()));
        reg.put("author", r.getAuthor());

        db.insert("rssitems", null, reg);
        
        if (db != null) db.close();
    }

    private String formatDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date));
    }

    private void deleteLastEntries() {

        SQLiteDatabase db = getReadableDb();
        Cursor c = db.rawQuery("SELECT count(*) FROM rssitems",null);

        int rowsNum = 0;

        if (c.moveToFirst()) {
            rowsNum = c.getInt(0);

            if (rowsNum > 100) {

                db = getWritableDb();
                int deletedRows = rowsNum - 100;
                String[] args = new String[] {String.valueOf(deletedRows)};
                db.execSQL("DELETE FROM rssitems ORDER BY pubDate ASC limit ?",args);
            }
        }

        if (db != null) db.close();
    }

    public List<RssItem> getList() {

        List<RssItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDb();

        Cursor c = db.rawQuery("SELECT title, category, description, url, guid, pubDate, author" +
                " FROM rssitems ORDER BY pubDate DESC limit 100",null);
        if (c.moveToFirst()) {
            do {
                RssItem r = new RssItem();

                r.setTitle(c.getString(0));
                r.setFirstCategory(c.getString(1));
                r.setDescription(c.getString(2));
                r.setUrl(c.getString(3));
                r.setGuid(c.getString(4));
                r.setPubDate(c.getString(5));
                r.setAuthor(c.getString(6));

                items.add(r);

            } while (c.moveToNext());
        }

        if (db != null) db.close();

        return items;
    }

    /*public void showData() {
        SQLiteDatabase db = getReadableDb();

        Cursor c = db.rawQuery("SELECT * FROM rssitems", null);
        if (c.moveToFirst()) {
            do {
                System.out.println(c.getString(0) + "x");
            } while (c.moveToNext());
        }
        if (db != null) db.close();
    }*/
}
