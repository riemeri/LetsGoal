package com.bignerdranch.android.letsgoal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.letsgoal.database.GoalBaseHelper;
import com.bignerdranch.android.letsgoal.database.GoalCursorWrapper;
import com.bignerdranch.android.letsgoal.database.GoalDbSchema;
import com.bignerdranch.android.letsgoal.database.GoalDbSchema.GoalTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ianri on 11/27/2017.
 */

public class GoalStorage {
    private static GoalStorage sGoalStorage;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static GoalStorage get(Context context) {
        if (sGoalStorage == null) {
            sGoalStorage = new GoalStorage(context);
        }
        return sGoalStorage;
    }

    private GoalStorage(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new GoalBaseHelper(mContext).getWritableDatabase();
    }

    public void addGoal(Goal g) {
        ContentValues values = getContentValues(g);

        mDatabase.insert(GoalTable.NAME, null, values);
    }

    public void deleteGoal(UUID id) {
        mDatabase.delete(GoalTable.NAME,
                GoalTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );
    }

    public List<Goal> getGoals() {
        List<Goal> goals = new ArrayList<>();

        GoalCursorWrapper cursor = queryGoals(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                goals.add(cursor.getGoal());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return goals;
    }

    public Goal getGoal(UUID id) {
        GoalCursorWrapper cursor = queryGoals(
                GoalTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getGoal();
        }
        finally {
            cursor.close();
        }
    }

    public Goal getLastOrNextCrime(Goal goal) {
        List<Goal> goals = getGoals();

        if (goals.size() > 1) {
            for (int i = 0; i < goals.size(); i++) {
                if (goals.get(i).getID().compareTo(goal.getID()) == 0) {
                    if (i == 0) {
                        return goals.get(1);
                    }
                    else {
                        return goals.get(i - 1);
                    }
                }
            }
        }

        return goal;
    }

    /*public File getPhotoFile(Goal goal) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, goal.getPhotoFileName());
    }*/

    public void updateCrime(Goal goal) {
        String uuidString = goal.getID().toString();
        ContentValues values = getContentValues(goal);

        mDatabase.update(GoalTable.NAME, values,
                GoalTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private GoalCursorWrapper queryGoals(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                GoalTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new GoalCursorWrapper(cursor);
    }

    public static ContentValues getContentValues(Goal goal) {
        ContentValues values = new ContentValues();
        values.put(GoalTable.Cols.UUID, goal.getID().toString());
        values.put(GoalTable.Cols.TITLE, goal.getTitle());
        values.put(GoalTable.Cols.DUEDATE, goal.getDueDate().getTime());
        values.put(GoalTable.Cols.NOTES, goal.getNotes());
        values.put(GoalTable.Cols.PROGRESS, goal.getProgress());
        values.put(GoalTable.Cols.COMPLETED, goal.isCompleted() ? 1 : 0);

        return values;
    }

}
