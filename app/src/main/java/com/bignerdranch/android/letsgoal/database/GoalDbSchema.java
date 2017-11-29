package com.bignerdranch.android.letsgoal.database;

/**
 * Created by ianri on 11/27/2017.
 */

public class GoalDbSchema {
    public static final class GoalTable {
        public static final String NAME = "goals";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DUEDATE = "duedate";
            public static final String PROGRESS = "progress";
            public static final String IMPORTANCE = "importance";
            public static final String NOTES = "notes";
            public static final String COMPLETED = "completed";
        }
    }
}
