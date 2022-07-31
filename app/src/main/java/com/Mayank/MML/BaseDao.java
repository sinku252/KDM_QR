package com.Mayank.MML;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;
@SuppressWarnings("unchecked")
public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T t);

    @Insert
    long[] insert(T... t);

    @Insert
    long[] insert(List<T> tList);

    @Delete
    int delete(T t);

    @Update
    int update(T t);

}
