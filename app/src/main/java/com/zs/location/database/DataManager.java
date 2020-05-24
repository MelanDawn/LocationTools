package com.zs.androidappfw.storage.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shoes on 2017/10/28.
 *
 */

public class DataManager {
    private static final String TAG = "DatabaseManager";

    private static final String DB_NAME = "location.db";
    private static final int DB_VERSION = 1;

    private AtomicInteger mDbCount = new AtomicInteger(0);
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;

    private static DataManager sInstance = null;
    private DataManager(Context context){
        mDbHelper = new DbHelper(context, DB_NAME, null, DB_VERSION);
    }

    public static DataManager getInstance(Context context) {
        if (sInstance == null){
            synchronized (DataManager.class){
                if (sInstance == null){
                    sInstance = new DataManager(context);
                }
            }
        }
        return sInstance;
    }

    public synchronized SQLiteDatabase openDb(){
        if (mDbCount.incrementAndGet() == 1) {
            mDb = mDbHelper.getWritableDatabase();
        }
        return mDb;
    }

    public synchronized void closeDb(){
        if (mDbCount.decrementAndGet() == 0){
            mDb.close();
        }
    }

    private class DbHelper extends SQLiteOpenHelper {
        DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context, name, factory, version);
        }

        DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
                 DatabaseErrorHandler errorHandler){
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}