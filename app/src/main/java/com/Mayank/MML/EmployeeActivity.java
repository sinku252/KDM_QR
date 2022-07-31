package com.Mayank.MML;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.os.HandlerCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmployeeActivity extends AppCompatActivity {

    private ListView listView;
    private EditText searchInput;
    private EmployeeListAdapter employeeListAdapter;
    private MyDatabase db;
    private EmployeeListAdapter listAdapter;
    //executorService to run the task in background thread
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    //handler to touch UI from background thread
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    Button scanitems;
    Button TakePhoto;
    LocationManager locationManager ;
    Double    latitude = 0.0;
    Double	 longitude = 0.0;
    LocationTrack gps;
    GPSTracker gpsTracker;
    String userChoosenTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        db = MyApplication.getMyDatabase();

        listView = findViewById(R.id.employeeListView);
        searchInput = findViewById(R.id.etSearchInput);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //fetchAllEmployees();
        searchInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                scan();
                // fetch data from server and insert into challan_det in local db
            }
        });
        //showData(listView);

        scanitems = (Button) findViewById(R.id.btnItems);

        // show location button click event
        scanitems.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(EmployeeActivity.this,ItemsScannedActivity.class);
                intent.putExtra("key",searchInput.getText());
                startActivity(intent);
                finish();
                // fetch data from server and insert into challan_det in local db
            }
        });


        TakePhoto = (Button) findViewById(R.id.btnReceipt);

        // show location button click event
        TakePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        showSettingsAlert();
                    } else {
                        try {
                            latitude = gpsTracker.getLatitude();
                            longitude = gpsTracker.getLongitude();
                        }catch (Exception e) {
                            // TODO: handle exception
                        }
                        selectImage1();
                    }
                }
                else {

                    if (gpsTracker.canGetLocation()) {
                        try{
                        latitude = gpsTracker.getLatitude();
                        longitude = gpsTracker.getLongitude();
                        }
                        catch (Exception e) {
                            // TODO: handle exception
                        }
                        selectImage1();
                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                }





                Intent intent=new Intent(EmployeeActivity.this,ItemsScannedActivity.class);
                intent.putExtra("key",searchInput.getText());
                startActivity(intent);
                finish();
                // fetch data from server and insert into challan_det in local db
            }
        });


    }

    private void fetchAllEmployees() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<Employee> employeeList = db.employeeDao().getAllEmployees();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        employeeListAdapter = new EmployeeListAdapter(EmployeeActivity.this, employeeList);
                        listView.setAdapter(employeeListAdapter);
                    }
                });

            }
        });
    }

    public void doSearch(View view) {
        final String query = searchInput.getText().toString().trim();
        if (TextUtils.isEmpty(query)){
            //fetchAllEmployees();
            Toast.makeText(this, "Please scan a challan to view items", Toast.LENGTH_LONG).show();
        }else{
            searchFilerData(query);
        }
    }

    private  void searchFilerData(final String query){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<Employee> employeeList = db.employeeDao().getAllEmployees(query);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        employeeListAdapter = new EmployeeListAdapter(EmployeeActivity.this, employeeList);
                        listView.setAdapter(employeeListAdapter);
                    }
                });

            }
        });
    }

    public void showData(View view) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<Employee> employeeList = db.employeeDao().getAllEmployees();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        employeeListAdapter = new EmployeeListAdapter(EmployeeActivity.this, employeeList);
                        listView.setAdapter(employeeListAdapter);
                    }
                });
            }
        });
    }


    public void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan Challan QR Code");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String scannedResult = result.getContents();
        searchInput.setText(scannedResult);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EmployeeActivity.this, CenterAudit.class);
        //Intent intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    public void showSettingsAlert(){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(EmployeeActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void selectImage1() {
        // TODO Auto-generated method stub
        final CharSequence[] items = { "Take Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent1(1);
                }
            }
        });
        builder.show();
    }

    private void cameraIntent1(int i1)
    {
        int i11=i1;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //Uri outputUri= FileProvider.getUriForFile(this, AUTHORITY, file);
            Uri outputUri = FileProvider.getUriForFile(EmployeeActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }
        startActivityForResult(intent, i11);
    }
}
