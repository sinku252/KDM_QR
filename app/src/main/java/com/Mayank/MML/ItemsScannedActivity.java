package com.Mayank.MML;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.HandlerCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemsScannedActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    //static SQLiteDatabase myDatabase;
    private CheckBox checkBox;
    Date currentTime;
    SimpleDateFormat df;
    ListView qrDetailList;
    List<QrBeanModel> QrObject = new ArrayList<>();
    QrDetailAdapter qrDetailAdapter;
    Cursor c;
    SearchView inputSearch;
    List<QrBeanModel> tempList = new ArrayList<>();
    boolean isConnected = false;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    Button scannow, delCh;
    //String res;
    int len, jlen, success;
    private EditText userName, email, number;
    private ListView listView;
    private ItemsScannedAdapter listAdapter;
    public String res;
    private MyDatabase db;

    //executorService to run the task in background thread
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    //handler to touch UI from background thread
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    JSONParser jParser = new JSONParser();
    private static String url = "http://localhost/kdm/fetch_challan.php";
    JSONArray animals = null;
    private ProgressDialog progressMessage;

    String userid2, pass2;
    String ch_no, adminUser, adminPass, invoice_no, di_ref, awc_code, awc_name, awc_add, id, pr_name, pr_code, pr_bags, pr_pouch = "";
    EditText pass, userid;
    Button login;

    String latitude = "0";
    String longitude = "0";
    String mcc = "0";
    String mnc = "0";
    String cid = "0";
    String lac = "0";
    String userm = "";

    private static final String TAG_SUCCESS = "success";
    Savedata savedata;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemsscanned);
        checkConnection();
        savedata=new Savedata(getApplicationContext());
        db = MyApplication.getMyDatabase();
        listView = findViewById(R.id.itemsScannedList);

        //Main code for search view
        tempList.addAll(QrObject);
        scannow = (Button) findViewById(R.id.button);

        // show location button click event
        scannow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                scan();
                // fetch data from server and insert into challan_det in local db
            }
        });
        /*String scannedResult1 = "000030/CBMM/P";
        int findex=scannedResult1.indexOf('/') + 1;
        String BP = scannedResult1.substring(scannedResult1.lastIndexOf("/") + 1);
        int lindex=scannedResult1.lastIndexOf('/');
        String IC = scannedResult1.substring(findex, lindex);
        Toast.makeText(this, "Scanned: " + scannedResult1 + "  findex :" + findex+ "  BP :" + BP+ "  lindex :" + lindex+ "  IC :" + IC, Toast.LENGTH_LONG).show();
*/
        gps = new GPSTracker(ItemsScannedActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
            @SuppressLint("MissingPermission") GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            String networkOperator = telephonyManager.getNetworkOperator();
            mcc = networkOperator.substring(0, 3); // vodafonr 404
            mnc = networkOperator.substring(3); // rajasthan 60
            cid = String.valueOf(cellLocation.getCid()); // 25436 gsm cell id
            lac = String.valueOf(cellLocation.getLac()); // 23711 GsmCellLocation
            userm = savedata.getuser();
            Toast.makeText(getApplicationContext(), "Fixed Parameters - \ngsm Mob No:" + String.valueOf(userm) +"\ngsm cell id: " + String.valueOf(cid) + "\ngsm location area code: " + String.valueOf(lac) + "\nmcc: " + mcc + "\nmnc: " + mnc + "\nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            //can check location on http://www.cell2gps.com/ with all 4 parameters
            String invoice = getIntent().getStringExtra("key");
            Toast.makeText(getApplicationContext(), "Challan No - :" + String.valueOf(invoice) , Toast.LENGTH_LONG).show();
        }else{
            // can't get location. GPS or Network is not enabled Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        showData(listView);
    }

    public void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan Items QR Code");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String scannedResult = result.getContents();
                String Lat = latitude;
                String Lon = longitude;
                String Mcc = mcc;
                String Mnc = mnc;
                String Cid = cid;
                String Lac = lac;
                String User = userm;

                int findex=scannedResult.indexOf('/') + 1;
                ///Toast.makeText(this, "Scanned: " + scannedResult + "findex :" + findex, Toast.LENGTH_LONG).show();
                String BP = scannedResult.substring(scannedResult.lastIndexOf("/") + 1);
                int lindex=scannedResult.lastIndexOf('/');
                String IC = scannedResult.substring(findex, lindex);


                final ItemsScanned emp = new ItemsScanned();
                emp.setInvoice_no(getIntent().getStringExtra("key"));
                emp.setPr_name(scannedResult);
                emp.setPr_code(IC);
                if (BP.equalsIgnoreCase("B")) {emp.setPr_bags(scannedResult);} else {emp.setPr_bags("");}
                if (BP.equalsIgnoreCase("P")) {emp.setPr_pouch(scannedResult);} else {emp.setPr_pouch("");}
                emp.setMobile_no(User);
                emp.setLat(Lat);
                emp.setLon(Lon);
                emp.setMcc(Mcc);
                emp.setMnc(Mnc);
                emp.setCid(Cid);
                emp.setLac(Lac);


                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        final long id = db.itemsScannedDao().insert(emp);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0) {
                                    Toast.makeText(ItemsScannedActivity.this, "Data Insertion success.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ItemsScannedActivity.this, "Data Insertion failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                ///new LoadAllProducts().execute();
                showData(listView);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


        private void checkConnection() {
            isConnected = ConnectivityReceiver.isConnected();
            showSnack(isConnected);
        }

        public void showData(View view) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    final List<ItemsScanned> itemsScannedList = db.itemsScannedDao().getAllItemsScanned();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter = new ItemsScannedAdapter(ItemsScannedActivity.this, itemsScannedList);
                            listView.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
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


        public class JSONParser {
            InputStream is = null;
            JSONObject jObj = null;
            String json = "";

            // constructor
            public JSONParser() {

            }

            // function get json from url
            // by making HTTP POST or GET mehtod
            public JSONObject makeHttpRequest(String url, String method,
                                              List<? extends NameValuePair> params) {

                // Making HTTP request
                try {

                    // check for request method
                    if (method == "POST") {
                        // request method is POST
                        // defaultHttpClient
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        httpPost.setEntity(new UrlEncodedFormEntity(params));

                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();

                    } else if (method == "GET") {
                        // request method is GET
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        //String paramString=URLDecoderUtils.format(params,"utf-8");
                        String paramString = URLEncodedUtils.format(params, "utf-8");
                        //String paramString = URLDecoder.decode("utf_8");
                        url += "?" + paramString;
                        HttpGet httpGet = new HttpGet(url);

                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "utf-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }

                // return JSON String
                return jObj;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ItemsScannedActivity.this, CenterAudit.class);
        //Intent intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    }