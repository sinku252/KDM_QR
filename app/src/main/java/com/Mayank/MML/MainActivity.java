package com.Mayank.MML;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import org.apache.http.message.BasicNameValuePair;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteStatement;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;


//public class MainActivity extends AppCompatActivity {
public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
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
    boolean isConnected;
    Button scannow, delCh;
    //String res;
    int len, jlen, success;

    private EditText userName, email, number;
    private ListView listView;
    private ListAdapter1 listAdapter;
    public String res;
    private MyDatabase db;

    //executorService to run the task in background thread
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    //handler to touch UI from background thread
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    JSONParser jParser = new JSONParser();
    private static String url = "https://www.gradientsoftech.com/mml/fetch_challan.php";
    JSONArray animals = null;

    private ProgressDialog progressMessage;

    String userid2, pass2;
    String ch_no, adminUser, adminPass, invoice_no, di_ref, awc_code, awc_name, awc_add, id, pr_name, pr_code, pr_bags, pr_pouch = "";
    EditText pass, userid;
    Button login;

    private static final String TAG_SUCCESS = "success";
    Savedata savedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();
//        intializeViews();
//       qrDetailAdapter = new QrDetailAdapter( this ,R.layout.qr_list_items,QrObject);
//        qrDetailList.setAdapter(qrDetailAdapter);
//        qrDetailAdapter.notifyDataSetChanged();
        //performSql();
        db = MyApplication.getMyDatabase();
        listView = findViewById(R.id.listview_data);

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
        showData(listView);
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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String scannedResult = result.getContents();

                res = scannedResult;
                currentTime = Calendar.getInstance().getTime();
                df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(currentTime);
                //Toast.makeText(this, "Date: " + formattedDate, Toast.LENGTH_LONG).show();
                String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                final User user = new User();
                user.setName(scannedResult);

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        final long id = db.userDao().insert(user);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0) {
                                    Toast.makeText(MainActivity.this, "Data Insertion success.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Data Insertion failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                new LoadAllProducts().execute();
                showData(listView);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    class LoadAllProducts extends AsyncTask<String, String, String> {
        String reviewmark, sdmsid;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sdmsid = res;
            //Log.e("asdfadsasd", sdmsid);
            //ProgressDialog progressMessage = new ProgressDialog(MainActivity.this);
            //progressMessage.setMessage("Loading...");
            //progressMessage.setCancelable(false);
            //progressMessage.show();
            // progressMessage.setIndeterminateDrawable(getApplicationContext().getResources().getDrawable(R.drawable.loading_animation));
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("chno", sdmsid));

            JSONObject json = jParser.makeHttpRequest(url, "GET", params);
            //Log.d("Create Response: ", json.toString());
            try {
                animals = json.getJSONArray("ch");
                for (int i = 0; i < animals.length(); i++) {
                    JSONObject word = animals.getJSONObject(i);

                    invoice_no = word.getString("invoice_no");
                    di_ref = word.getString("di_ref");
                    awc_code = word.getString("aw_code");
                    awc_name = word.getString("aw_name");
                    awc_add = word.getString("aw_add");
                    id = word.getString("id");
                    pr_name = word.getString("pr_name");
                    pr_code = word.getString("pr_code");
                    pr_bags = word.getString("pr_bags");
                    pr_pouch = word.getString("pr_pouch");
                    //Toast.makeText(MainActivity.this, "Product = "+ pr_name, Toast.LENGTH_SHORT).show();

                    final List<Employee> employeeList = db.employeeDao().getAllEmployees();
                    for(int j=0;j<employeeList.size();j++)
                    {
                        Employee employee=employeeList.get(j);
                        if(!employee.getInvoice_no().equalsIgnoreCase(invoice_no))
                        {
                            final Employee emp = new Employee();
                            emp.setInvoice_no(invoice_no);
                            emp.setDi_ref(di_ref);
                            emp.setAwc_code(awc_code);
                            emp.setAwc_name(awc_name);
                            emp.setAwc_add(awc_add);
                            emp.setChid(id);
                            emp.setPr_name(pr_name);
                            emp.setPr_code(pr_code);
                            emp.setPr_bags(pr_bags);
                            emp.setPr_pouch(pr_pouch);

                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    final long id = db.employeeDao().insert(emp);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (id > 0) {
                                                //Toast.makeText(MainActivity.this, "Data Insertion success 2.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                //Toast.makeText(MainActivity.this, "Data Insertion failed 2.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "QR Code already scanned", Toast.LENGTH_LONG).show();
                        }
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url){
            //progressMessage.dismiss();
            Toast.makeText(MainActivity.this, "Challan Details Added", Toast.LENGTH_SHORT).show();
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
                    final List<User> userList = db.userDao().getAllUsers();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter = new ListAdapter1(MainActivity.this, userList);
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
        Intent intent = new Intent(MainActivity.this, CenterAudit.class);
        //Intent intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    }