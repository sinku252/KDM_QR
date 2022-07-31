package com.Mayank.MML;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import android.support.design.widget.Snackbar;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.AppCompatButton;

//@SuppressWarnings("deprecation")
public class Login extends RuntimePermissionsActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
	private Button buttonConfirm;
	static final Integer CAMERA = 0x5;
	static final Integer LOCATION = 0x1;
	private static final int REQUEST_PERMISSIONS = 20;
	private Boolean loading = false;
	JSONParser jParser = new JSONParser();
	//private static String url = "http://localhost/kdm/login_new.php";

	JSONArray animals = null;
	private ProgressDialog pDialog;
	private PrefManager prefManager;
	int len, success;
	String userid2,pass2;
	String adminUser,adminPass;
	EditText pass,userid;
	Button login;
	String msgotp;
	private static final String TAG_SUCCESS="success";
	Savedata savedata;
	boolean isConnected;
	private EditText editTextConfirmOtp;
	EditText ed5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		checkConnection();
		initpDialog();

		ed5=(EditText)findViewById(R.id.userid);

		prefManager = new PrefManager(this);
		if (!prefManager.isFirstTimeLaunch()) {
			launchHomeScreen();
			finish();
		}

		savedata=new Savedata(Login.this);

		userid=(EditText)findViewById(R.id.userid);
		login=(Button)findViewById(R.id.login);
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

				if(userid.length()==0)
				{
					 Toast.makeText(Login.this.getApplication(),"Please Enter Your Mobile No ", Toast.LENGTH_SHORT).show();
				}
				else {
					
						try
						{
							if (isConnected)
							{
								savedata.save_user(userid.getText().toString());
								SendData();
								//new LoadAllProducts().execute();
							}
							else
							{
								showSnack(isConnected);
							}

						}
						catch (Exception e) {
							// TODO: handle exception
						}

				}

			}
		});

	}




	public void SendData() {
		loading = true;
		showpDialog();
		CustomRequest jsonReq = new CustomRequest(Request.Method.POST, Constants.INSERT, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.e("response",""+response);

						loading = false;
						hidepDialog();
						try
						{

							if(response.getBoolean("success"))
							{
								storeRegIdInPre(ed5.getText().toString());

								confirmOtp(response.getString("otp"));
							}
							else
							{
								Toast.makeText(Login.this,"Something wrong!",Toast.LENGTH_LONG).show();
							}

							//   launchHomeScreen();
						}
						catch (Exception e)
						{
							Log.e("afasf",""+e);

						}


					}
				}, new Response.ErrorListener() {

			public void onErrorResponse(VolleyError error) {
				Log.e("error",""+error);
				Toast.makeText(Login.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
				loading = false;
				hidepDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("contact_no",  ed5.getText().toString());
				return checkParams(params);
			}

			private Map<String,String> checkParams(Map<String,String> map)
			{
				Iterator<Map.Entry<String,String>> lt=map.entrySet().iterator();
				while (lt.hasNext())
				{
					Map.Entry<String,String> pairs=(Map.Entry<String,String>)lt.next();
					if(pairs.getValue()==null)
					{
						map.put(pairs.getKey(),"");
					}
				}
				return map;
			}

		};


		jsonReq.setRetryPolicy(new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		com.Mayank.MML.MyApplication.getInstance().addToRequestQueue(jsonReq);


	}



	private void confirmOtp(String parm) throws JSONException {

		final  String parm1=parm;

		//Creating a LayoutInflater object for the dialog box
		LayoutInflater li = LayoutInflater.from(Login.this);
		//Creating a view to get the dialog box
		View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

		//Initizliaing confirm button fo dialog box and edittext of dialog box
		buttonConfirm = confirmDialog.findViewById(R.id.buttonConfirm);
		editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

		//Creating an alertdialog builder
		AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);

		//Adding our dialog box to the view of alert dialog
		alert.setView(confirmDialog);

		//Creating an alert dialog
		final AlertDialog alertDialog = alert.create();

		//Displaying the alert dialog
		alertDialog.show();

		alertDialog.setCancelable(false);

		//On the click of the confirm button from alert dialog
		buttonConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Hiding the alert dialog
				loading = true;
				showpDialog();

				//Displaying a progressbar
				//Getting the user entered otp from edittext
				final String otp = editTextConfirmOtp.getText().toString().trim();

				//Toast.makeText(getActivity(),otp,Toast.LENGTH_LONG).show();
				editTextConfirmOtp.setText(msgotp);

				if(otp.length()>0) {

					if (parm1.equalsIgnoreCase(otp)) {
						loading = false;
						hidepDialog();
						alertDialog.dismiss();
						launchHomeScreen();
					} else if (!parm1.equalsIgnoreCase(otp)) {
						loading = false;
						hidepDialog();
						Toast.makeText(Login.this, "Otp was wrong", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					loading = false;

					hidepDialog();
					Toast.makeText(Login.this, "Please enter otp", Toast.LENGTH_LONG).show();
				}



			}
		});
	}



	@Override
	public void onNetworkConnectionChanged(boolean isConnected) {
		showSnack(isConnected);
	}

	public class JSONParser{
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
				if(method == "POST"){
					// request method is POST
					// defaultHttpClient
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url);
					httpPost.setEntity(new UrlEncodedFormEntity(params));

					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					is = httpEntity.getContent();

				}else if(method == "GET"){
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

	// Method to manually check connection status
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

	public void showSettingsAlert(){

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
		// Setting Dialog Title
		alertDialog.setTitle("GPS settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// Setting Icon to Dialog
		//alertDialog.setIcon(R.drawable.delete);

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
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



	/*ask for permission*/

	@Override
	public void onPermissionsGranted(int requestCode) {

	}
	protected void initpDialog() {

		pDialog = new ProgressDialog(Login.this);

		//pDialog.setMessage(getString(R.string.loading));
		pDialog.show();
		pDialog.setCancelable(false);
		pDialog.dismiss();
	}

	protected void showpDialog() {

		if (!pDialog.isShowing()) pDialog.show();pDialog.dismiss();
	}

	protected void hidepDialog() {

		if (pDialog.isShowing()) pDialog.dismiss();
	}

	private void launchHomeScreen() {
		prefManager.setFirstTimeLaunch(false);
		startActivity(new Intent(Login.this, CenterAudit.class));
		//finish();
	}

	private void storeRegIdInPre(String token) {
		SharedPreferences pref = getApplicationContext().getSharedPreferences("kdm", 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("mob", token);
		editor.commit();
	}

}
