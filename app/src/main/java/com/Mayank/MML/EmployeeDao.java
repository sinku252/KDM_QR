package com.Mayank.MML;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmployeeDao extends BaseDao<com.Mayank.MML.Employee> {

    @Query("select * from challan_det")
    List<com.Mayank.MML.Employee> getAllEmployees();


    @Query("select * from challan_det where invoice_no= :query")
    List<com.Mayank.MML.Employee> getAllEmployees(String query);


    @Query("delete from challan_det where invoice_no= :query")
    void deleteEmployee(String query);
}
