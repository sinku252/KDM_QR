package com.Mayank.MML;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "challan_uploaded", ignoredColumns = {"isSelected","updatedDate"})
public class ItemsScanned {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int id;
    @ColumnInfo(name = "invoice_no")
    private String invoice_no;
    @ColumnInfo(name = "pr_name")
    private String pr_name;
    @ColumnInfo(name = "pr_code")
    private String pr_code;
    @ColumnInfo(name = "pr_bags")
    private String pr_bags;
    @ColumnInfo(name = "pr_pouch")
    private String pr_pouch;
    @ColumnInfo(name = "mobile_no")
    private String mobile_no;
    @ColumnInfo(name = "lat")
    private String lat;
    @ColumnInfo(name = "lon")
    private String lon;
    @ColumnInfo(name = "mcc")
    private String mcc;
    @ColumnInfo(name = "mnc")
    private String mnc;
    @ColumnInfo(name = "cid")
    private String cid;
    @ColumnInfo(name = "lac")
    private String lac;


    public boolean isSelected;
    private long updatedDate;

    @ColumnInfo(name = "createdDate")
    public long createdDate;

    public String getInvoice_no() { return invoice_no; }
    public void setInvoice_no(String invoice_no) { this.invoice_no = invoice_no; }

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

    public String getMobile_no() { return mobile_no; }
    public void setMobile_no(String mobile_no) { this.mobile_no = mobile_no; }

    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getMcc() {
        return mcc;
    }
    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }
    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getCid() {
        return cid;
    }
    public void setCid(String pr_bags) {
        this.cid = cid;
    }

    public String getLac() {
        return lac;
    }
    public void setLac(String lac) {
        this.lac = lac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
