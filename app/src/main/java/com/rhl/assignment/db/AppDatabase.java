package com.rhl.assignment.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.rhl.assignment.bean.FileBean;

@Database(entities = {FileBean.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FileDao fileDao();
}
