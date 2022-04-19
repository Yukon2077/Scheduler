package com.scheduler.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final Integer DB_VERSION = 1;
    private static final String DB_NAME = "ReminderDB";
    private static final String TABLE_NAME = "ReminderTable";
    public static final String COL_ID = "_id";
    public static final String COL_TEXT = "REMINDER_TEXT";
    public static final String COL_DATE = "SCHEDULED_DATE";
    public static final String COL_TIME = "SCHEDULED_TIME";
    public static final String COL_RECURRING = "RECURRING_DATETIME";

    public ReminderSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TEXT + " TEXT, " +
                COL_DATE + " DATE, " +
                COL_TIME + " TIME, " +
                COL_RECURRING + " TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
        onCreate(db);
    }

    public void addReminder() {

    }

    public Cursor getReminder(SQLiteDatabase db) {
        return  db.query( TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COL_DATE + " DESC");
    }
}
