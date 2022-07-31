package com.Mayank.MML;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao extends BaseDao<com.Mayank.MML.User>{
    @Query("select * from qr_det")
    List<com.Mayank.MML.User> getAllUsers();

    @Query("delete from qr_det where name= :query")
    void deleteUser(String query);
}
