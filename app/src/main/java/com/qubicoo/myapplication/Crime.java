package com.qubicoo.myapplication;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String Suspect;
    private String mTitle;
    private Date mDate;  /// KIO: used java.util and not java.sql, in case this causes errors later
    private boolean mSolved;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID uuid) {
        mId = uuid;
        mDate = new Date();
    }

    // Override this value so that when our ArrayAdapter grabs these guys to display, it can
    //   print out some meaningful text instead of the class name and memory address (default)
    @Override
    public String toString() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getSuspect() { return Suspect; }

    public void setSuspect(String suspect) { Suspect = suspect; }

    public boolean isSolved() { return mSolved; }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPhotoFileName(){
        return "IMG_" + getId().toString()+ ".jpg";
    }
    public void setTitle(String title) {
        mTitle = title;
    }
}
