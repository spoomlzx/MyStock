package com.spoom.xiaohei.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * package com.lan.ichat.manager.user
 *
 * @author spoomlan
 * @date 19/12/2017
 */

public class IMDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static IMDatabaseHelper instance;

    public IMDatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory) {
        super(context, databaseName, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE friend (username text NOT NULL PRIMARY KEY,nickname text,type integer NOT NULL,telephone text,motto text,avatar text,gender integer,region text,remark text,localAvatar text,bigAvatar text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE friend (username text NOT NULL PRIMARY KEY,nickname text,type integer NOT NULL,telephone text,motto text,avatar text,gender integer,region text,remark text,localAvatar text,bigAvatar text);");
    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
