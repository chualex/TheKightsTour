package com.csci448.alchu.thekightstour;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alex on 3/19/18.
 */

public class GameBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "records.db";
    public GameBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + GameDbSchema.RecordTable.NAME+ "(" +
                    GameDbSchema.RecordTable.Cols.BOARDSIZE + " INT PRIMARY KEY, "
                    + GameDbSchema.RecordTable.Cols.NAME + " TEXT, "
                    + GameDbSchema.RecordTable.Cols.TIME + " DOUBLE"
                    + ")"
            );

            db.execSQL("select * from " + GameDbSchema.RecordTable.NAME + " where 'boardsize'=5 ");

            db.execSQL("INSERT INTO " + GameDbSchema.RecordTable.NAME + " (boardsize, name, time) " +
            "VALUES " +
            "(5, '', 999.9), " +
            "(6, '', 999.9), " +
            "(7, '', 999.9), " +
            "(8, '', 999.9), " +
            "(9, '', 999.9), " +
            "(10, '', 999.9)"
            );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

