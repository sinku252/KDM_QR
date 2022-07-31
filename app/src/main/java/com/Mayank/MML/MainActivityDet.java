package com.Mayank.MML;

import android.app.Activity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//public class MainActivity extends AppCompatActivity {
public class MainActivityDet extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener {
    static SQLiteDatabase myDatabase;
    ListView qrDetailList;
    List<QrBeanModelDet> QrObject = new ArrayList<>();
    QrDetailAdapterDet qrDetailAdapterDet;
    Cursor c;
    List<QrBeanModelDet> tempList = new ArrayList<>();
    boolean isConnected;


    private static final String TAG_SUCCESS="success";
    Savedata savedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_det);
        checkConnection();
        intializeViews();
        qrDetailAdapterDet = new QrDetailAdapterDet( this ,R.layout.qr_challan_items,QrObject);
        qrDetailList.setAdapter(qrDetailAdapterDet);
        qrDetailAdapterDet.notifyDataSetChanged();
        performSql();

        //Main code for search view
        tempList.addAll(QrObject);

    }
    public int numberOfRowsRecord() {
        myDatabase =  this .openOrCreateDatabase( "Users" , MODE_PRIVATE , null );
        return (int) DatabaseUtils.queryNumEntries(myDatabase, "challan_det");
    }

    public void intializeViews(){
        qrDetailList = (ListView) findViewById(R.id.qrDetailList);
        qrDetailList.setTextFilterEnabled(true);
        //inputSearch = (SearchView) findViewById(R.id.inputSearch);
    }

     public void performSql(){
        myDatabase = this .openOrCreateDatabase( "Users" , MODE_PRIVATE , null );
        //Creating the table if not exists
        showDatabaseInList();
    }

    public void showDatabaseInList(){

        try {
            c = myDatabase.rawQuery("SELECT * FROM challan_det",null);

            int invoice_no1 = c.getColumnIndex("invoice_no");
            int item_code1 = c.getColumnIndex("item_code");
            int item_name1 = c.getColumnIndex("item_name");
            int bag1 = c.getColumnIndex("bag");
            int pouch1 = c.getColumnIndex("pouch");

            if(c.moveToFirst()){
                do{
                    Log. i ( "user-name" ,c.getString(invoice_no1));
                    //Log.i("date id ",c.getString(dateIDIndex));

                    QrBeanModelDet qrBeanModelDet = new QrBeanModelDet(c.getString(invoice_no1),c.getString(item_code1),c.getString(item_name1),c.getInt(bag1),c.getInt(pouch1));

                    QrObject.add(qrBeanModelDet);
                    qrDetailAdapterDet.notifyDataSetChanged();
                    //Log. i ( "user-age" ,c.getString(dateIndex));
                }while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    private void checkConnection() {
        isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }



    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar bar = Snackbar.make(findViewById(android.R.id.content).getRootView(), message, Snackbar.LENGTH_LONG);

            View sbView = bar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(color);
            bar.show();

        }

    }
    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}
