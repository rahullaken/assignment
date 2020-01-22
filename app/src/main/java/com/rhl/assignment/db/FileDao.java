package com.rhl.assignment.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.rhl.assignment.bean.FileBean;

import java.util.List;

@Dao
public interface FileDao {
    @Query("SELECT * FROM filebean")
    List<FileBean> getAll();

    @Insert
    void insert(FileBean fileBean);

    @Delete
    void delete(FileBean user);
}
