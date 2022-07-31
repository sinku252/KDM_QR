package com.Mayank.MML;

public class QrBeanModelDet {
    private String QrText, ChItCode, ChItem;
    private String Date;
    private Integer ChBags, ChPouch;
    public String SpecDate;

    public QrBeanModelDet(String qrText, String chitcode, String chitem, Integer chbags, Integer chpouch) {
        QrText = qrText;
        ChItCode = chitcode;
        ChItem = chitem;
        ChBags = chbags;
        ChPouch = chpouch;
    }

    /*public String getSpecDate() { return SpecDate; }

    public void setSpecDate(String specDate) {
        SpecDate = specDate;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }*/

    public String getQrText() {
        return QrText;
    }
    public void setQrText(String qrText) {
        QrText = qrText;
    }
    public String getChItCode() {
        return ChItCode;
    }
    public void setChItCode(String chitcode) {
        ChItCode = chitcode;
    }
    public String getChItem() {
        return ChItCode;
    }
    public void setChItem(String chitem) {
        ChItCode = chitem;
    }

    public Integer getChBags() {
        return ChBags;
    }
    public void setChBags(Integer chbags) {ChBags = chbags; }
    public Integer getChPouch() {
        return ChPouch;
    }
    public void setChPouch(Integer chpouch) {
        ChPouch = chpouch;
    }
}
