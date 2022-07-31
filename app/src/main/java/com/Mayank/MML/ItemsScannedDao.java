package com.Mayank.MML;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemsScannedDao extends BaseDao<ItemsScanned> {

    @Query("select * from challan_uploaded")
    List<ItemsScanned> getAllItemsScanned();


    @Query("select * from challan_uploaded where invoice_no= :query")
    List<ItemsScanned> getAllItemsScanned(String query);


    @Query("delete from challan_uploaded where invoice_no= :query")
    void deleteItemsScanned(String query);
}
