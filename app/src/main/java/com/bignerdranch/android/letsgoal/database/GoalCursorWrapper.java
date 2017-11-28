package com.bignerdranch.android.letsgoal.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.letsgoal.Goal;
import com.bignerdranch.android.letsgoal.database.GoalDbSchema.GoalTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ianri on 11/27/2017.
 */

public class GoalCursorWrapper extends CursorWrapper {
    public GoalCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Goal getGoal() {
        String uuidString = getString(getColumnIndex(GoalTable.Cols.UUID));
        String title = getString(getColumnIndex(GoalTable.Cols.TITLE));
        long dueDate = getLong(getColumnIndex(GoalTable.Cols.DUEDATE));
        String notes = getString(getColumnIndex(GoalTable.Cols.NOTES));
        int progress = getInt(getColumnIndex(GoalTable.Cols.PROGRESS));
        int isCompleted = getInt(getColumnIndex(GoalTable.Cols.COMPLETED));

        Goal goal = new Goal(UUID.fromString(uuidString));
        goal.setTitle(title);
        goal.setDueDate(new Date(dueDate));
        goal.setNotes(notes);
        goal.setProgress(progress);
        goal.setCompleted(isCompleted != 0);

        return goal;
    }
}
