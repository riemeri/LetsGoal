package com.bignerdranch.android.letsgoal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.letsgoal.database.GoalDbSchema.GoalTable;

/**
 * Created by ianri on 11/27/2017.
 */

public class GoalBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public GoalBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + GoalTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                GoalTable.Cols.UUID + ", " +
                GoalTable.Cols.TITLE + ", " +
                GoalTable.Cols.DUEDATE + ", " +
                GoalTable.Cols.NOTES + ", " +
                GoalTable.Cols.PROGRESS + ", " +
                GoalTable.Cols.IMPORTANCE + ", " +
                GoalTable.Cols.COMPLETED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
