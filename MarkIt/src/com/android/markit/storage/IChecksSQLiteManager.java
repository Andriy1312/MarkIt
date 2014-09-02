package com.android.markit.storage;

import java.util.ArrayList;

import com.android.markit.entry.Mark;

public interface IChecksSQLiteManager {

    public long putValues(Mark mark);

    public ArrayList<Mark> getValues();

    public void deleteRow(int rawId);
}