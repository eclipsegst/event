package com.example.android.event.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.event.data.NotaContract.CategoryEntry;
import com.example.android.event.data.NotaContract.NotaEntry;

/**
 * Manage a local database for nota data.
 */
public class NotaDbHelper extends SQLiteOpenHelper {

    // We must increment the database version if we change the database schema.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "nota.db";

    public NotaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold categories.
        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                CategoryEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                CategoryEntry.COLUMN_TOTAL_TIME + " INTEGER NULL" +
                " );";

        final String SQL_CREATE_NOTA_TABLE = "CREATE TABLE " + NotaEntry.TABLE_NAME + " (" +
                NotaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                NotaEntry.COLUMN_CAT_KEY + " INTEGER NOT NULL, " +
                NotaEntry.COLUMN_SUBJECT + " TEXT NOT NULL, " +
                NotaEntry.COLUMN_NOTE + " TEXT, " +
                NotaEntry.COLUMN_START + " INTEGER NOT NULL, " +
                NotaEntry.COLUMN_END + " INTEGER NOT NULL, " +
                NotaEntry.COLUMN_DURATION + " INTEGER NOT NULL, " +
                NotaEntry.COLUMN_LAT + " REAL NOT NULL, " +
                NotaEntry.COLUMN_LON + " REAL NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // We simply to discard the data and start over.
        // This only fires if we change the version number for the database.
        // If we want to update the schema without wiping data, commenting out the next two lines
        // first.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NotaEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
