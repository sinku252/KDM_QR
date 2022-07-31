package com.Mayank.MML;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Employee.class, ItemsScanned.class}, version = 2, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EmployeeDao employeeDao();
    public abstract ItemsScannedDao itemsScannedDao();
}
