package com.faayda.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Aashutosh @ vinove on 8/4/2015.
 */
public class DBHelper {
    private SQLiteDatabase db;
    private final Context context;
    private final AppData dbHelper;
    public static int no;
    public static DBHelper db_helper = null;

    public static DBHelper getInstance(Context context) {
        try {
            if (db_helper == null) {
                db_helper = new DBHelper(context);
                db_helper.open();
            }
        } catch (IllegalStateException e) {
            //db_helper already open
        }
        return db_helper;
    }

	/*
     * set context of the class and initialize AppData Object
	 */

    public DBHelper(Context c) {
        context = c;
        dbHelper = new AppData(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
    }

    /*
     * Close Database.
     */
    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }

    /*
     * open database
     */
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.v("open database", "error=" + e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }

    public void deleteRecords(String table, String whereClause, String[] whereArgs) {
        db.delete(table, whereClause, whereArgs);
    }

    public void deleteRawQuery(String query) {
        db.rawQuery(query, null).moveToFirst();
    }

    public boolean dbOpenCheck() {
        try {
            return db.isOpen();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * @param table
     * @param column
     * @param where
     * @param orderBy
     * @param limit
     * @return
     */
    public String getValue(String table, String column, String where, String orderBy, String limit) {

        // DBConstants.SMS_DATE DESC
        if (db != null)
            if (!db.isOpen())
                open();
        Cursor result = db.query(false, table, new String[]{column}, where, null, null, null, orderBy, limit);

        String value = "";
        try {
            if (result.moveToFirst()) {
                value = result.getString(0);
            } else {
                return null;
            }
        } finally {
            if (result != null && !result.isClosed())
                result.close();
        }
        return value;
    }

    /**
     * @param table
     * @param column
     * @param where
     * @return
     */
    public String getValue(String table, String column, String where) {
        return getValue(table, column, where, DBConstants.ID, null);
    }

    /*
     * Get Multiple Values from column of any specified table.
	 */

    public String[] getValues(boolean b, String table, String column, String where, String orderBy) {
        ArrayList<String> savedAns = new ArrayList<String>();
        Cursor result = null;
        String[] colVals;
        if (!db.isOpen())
            open();
        try {
            result = db.query(b, table, new String[]{column}, where, null, null, null, orderBy, null);
            if (result.moveToFirst()) {
                do {
                    savedAns.add(result.getString(result.getColumnIndex(column)));
                } while (result.moveToNext());
            } else {
                return new String[0];
            }
            colVals = savedAns.toArray(new String[result.getCount()]);
        } finally {
            if (result != null && !result.isClosed())
                result.close();
        }
        return colVals;
    }

    public Cursor getTableRecords(String tablename, String[] columns, String where, String orderby) {
        Cursor cursor = db.query(false, tablename, columns, where, null, null, null, orderby, null);
        return cursor;
    }

    public Cursor getTableRecords(String tablename, String[] columns, String where, String orderby, String limit) {
        Cursor cursor = db.query(false, tablename, columns, where, null, null, null, orderby, limit);
        return cursor;
    }

    public int updateRecords(String table, ContentValues values, String whereClause, String[] whereArgs) {
        int a = db.update(table, values, whereClause, whereArgs);
        return a;
    }

    public long insertContentVals(String tableName, ContentValues content) {
        long id = 0;
        try {
            id = db.insert(tableName, null, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public int getTableRecordsCount(String tablename, String[] columns, String where, String orderby) {
        Cursor cursor = getTableRecords(tablename, columns, where, orderby);
      /*  int count = cursor.getCount();
        count = count > 0 ? count : 0;*/

        return cursor.getCount();
    }

    public Cursor getDataThroughRaw(String query) {


        Cursor cursor = db.rawQuery(query, null);
        return cursor;


    }

}