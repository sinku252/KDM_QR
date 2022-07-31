package com.Mayank.MML;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "qr_det", ignoredColumns = {"isSelected","updatedDate"})
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;


    public boolean isSelected;
    private long updatedDate;

    @ColumnInfo(name = "createdDate")
    public long createdDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
