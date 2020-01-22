package com.rhl.assignment.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FileBean {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "file_name")
    public String fileName;

    @ColumnInfo(name = "lat")
    public Double lat;

    @ColumnInfo(name = "longi")
    public Double longi;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }
}
