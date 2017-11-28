package com.bignerdranch.android.letsgoal;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ianri on 11/26/2017.
 */

public class Goal {

    private UUID mID;
    private String mTitle;
    private Date mDueDate;
    private int mProgress;
    private String mNotes;
    private boolean mCompleted;

    public Goal() {
        this(UUID.randomUUID());
    }

    public Goal(UUID id) {
        mID = id;
        mDueDate = new Date();
    }

    public int getDaysLeft() {
        Date date = new Date();
        long timeDif = mDueDate.getTime() - date.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeDif);
        int daysLeft = calendar.get(Calendar.DAY_OF_YEAR);

        return daysLeft;
    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }
}
