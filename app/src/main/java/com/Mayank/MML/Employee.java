package com.Mayank.MML;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "challan_det", ignoredColumns = {"isSelected","updatedDate"})
public class Employee {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int id;
    @ColumnInfo(name = "invoice_no")
    private String invoice_no;
    @ColumnInfo(name = "di_ref")
    private String di_ref;
    @ColumnInfo(name = "awc_code")
    private String awc_code;
    @ColumnInfo(name = "awc_name")
    private String awc_name;
    @ColumnInfo(name = "awc_add")
    private String awc_add;
    @ColumnInfo(name = "chid")
    private String chid;
    @ColumnInfo(name = "pr_name")
    private String pr_name;
    @ColumnInfo(name = "pr_code")
    private String pr_code;
    @ColumnInfo(name = "pr_bags")
    private String pr_bags;
    @ColumnInfo(name = "pr_pouch")
    private String pr_pouch;


    public boolean isSelected;
    private long updatedDate;

    @ColumnInfo(name = "createdDate")
    public long createdDate;

    public String getInvoice_no() {
        return invoice_no;
    }
    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getDi_ref() {
        return di_ref;
    }
    public void setDi_ref(String di_ref) {
        this.di_ref = di_ref;
    }

    public String getAwc_code() {
        return awc_code;
    }
    public void setAwc_code(String awc_code) {
        this.awc_code = awc_code;
    }

    public String getAwc_name() {
        return awc_name;
    }
    public void setAwc_name(String awc_name) {
        this.awc_name = awc_name;
    }

    public String getAwc_add() {
        return awc_add;
    }
    public void setAwc_add(String awc_add) {
        this.awc_add = awc_add;
    }

    public String getChid() {
        return chid;
    }
    public void setChid(String chid) {
        this.chid = chid;
    }

    public String getPr_name() {
        return pr_name;
    }
    public void setPr_name(String pr_name) {
        this.pr_name = pr_name;
    }

    public String getPr_code() {
        return pr_code;
    }
    public void setPr_code(String pr_code) {
        this.pr_code = pr_code;
    }

    public String getPr_bags() {
        return pr_bags;
    }
    public void setPr_bags(String pr_bags) {
        this.pr_bags = pr_bags;
    }

    public String getPr_pouch() {
        return pr_pouch;
    }
    public void setPr_pouch(String pr_pouch) {
        this.pr_pouch = pr_pouch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
