package com.Mayank.MML;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

//import android.support.design.widget.Snackbar;

public class CenterAudit extends RuntimePermissionsActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {

	
	//AlertDialog.Builder alertDialogBuilder;
      Button scanch, scanit, sync, exit;
      String success,message,random_number,center_code;
      private ProgressDialog progressMessage;
		private MyDatabase db;
      String user_id;

	private static final int REQUEST_PERMISSIONS = 20;

	boolean isConnected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_center_audit);
		db = MyApplication.getMyDatabase();
		checkConnection();
			CenterAudit.super.requestAppPermissions(new
						String[]{Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE}, R.string
						.runtime_permissions_txt
				, REQUEST_PERMISSIONS);



		
		Intent intent=getIntent();
		
		user_id=intent.getStringExtra("user_id");
		
		scanch=(Button)findViewById(R.id.ScanChallan);
		scanit=(Button)findViewById(R.id.scanItems);
		sync=(Button)findViewById(R.id.syncauditcenters);
		exit=(Button)findViewById(R.id.exit);

		sync.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//db.clearAllTables();

				Intent intent=new Intent(CenterAudit.this,DeleteActivity.class);
				startActivity(intent);
				finish();
				/*try {
					db.clearAllTables();
				} catch (Exception e){
					Toast.makeText(CenterAudit.this, "Exception" + e.toString(), Toast.LENGTH_LONG).show();
				}*/
			}
		});

		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	AlertDialog diaBox = makeAndShowDialogBox();
				
			    //diaBox.show();

				if(getFragmentManager().getBackStackEntryCount()>2){
					finish();
				}
						else
						{
							getFragmentManager().popBackStackImmediate();
							new AlertDialog.Builder(CenterAudit.this)
							.setTitle("Close App")
							.setMessage("Do you really want to close this app?")
							.setNegativeButton("No",null)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									
								 	Intent intent=new Intent(Intent.ACTION_MAIN);
								 	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								 	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								 	finish();
						        	android.os.Process.killProcess(android.os.Process.myPid());   
						        //	moveTaskToBack(true);
					  	            System.exit(0);
					               
								}
							}).create().show();
							//moveTaskToBack(true);
						}
				
			}
		});
		scanch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (isConnected)
							{
								Intent intent=new Intent(CenterAudit.this,MainActivity.class);
								startActivity(intent);
								finish();
							}
							else
							{
								showSnack(isConnected);
							}
						}
					});

		scanit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(CenterAudit.this,EmployeeActivity.class);
				startActivity(intent);
				finish();
			}
		});
    }

	@Override
	public void onPermissionsGranted(int requestCode) {

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

	class LoadAllProducts extends AsyncTask<String, String, String> {

        String temp1="hello";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressMessage = new ProgressDialog(CenterAudit.this);
            progressMessage.setMessage("Loading...");
            progressMessage.setCancelable(false);
            progressMessage.show();
            }





        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub


            try
            {
            	String url = "http://localhost/kdm/random_number.php";
				RestClient r=new RestClient(url);
                 
                r.AddParam("user_id",user_id);
                
             


                try
                {
                    r.Execute(RequestMethod.POST);
                }
                catch(Exception e1)
                {
                    Log.e("exception", "" + e1);
                }

                String res=r.getResponse();

                
                Log.e("plspls",""+res);


                JSONObject j=new JSONObject(res);
             
                
                
             
                message=j.getString("message");
                success=j.getString("success");   
                random_number=j.getString("random"); 
                
                center_code=j.getString("center_code");
                
                
                
                
                Log.e("random_number", "" + random_number);
                
                
                
               

            }
            catch (Exception e) {
                // TODO: handle exception
                Log.d("what is the error",""+e);
            }
            return null;
        }


        protected void onPostExecute(String file_url){

            progressMessage.dismiss();
            Log.e("random_number_one", "" + random_number);

            	//finish();
           /* userid=j.getString("userid");
            name=j.getString("name");
            mail_id=j.getString("mail_id");
            name=j.getString("name");
            success=j.getString("success");
            
            message=j.getString("message");*/
            
            try
            {
            	 /*if(success.equalsIgnoreCase("1"))
                 {
                 	
     	            Intent intent = new Intent(CenterAudit.this, CenterAuditDetails1.class);
     	            intent.putExtra("random", random_number);
     	           	intent.putExtra("center_code", center_code);
                    startActivity(intent);
     				//finish();
     	            
                 }
                 else*/
                 {
                 	Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                 }
            }
            catch (Exception e) {
				Log.e("exception",""+e);
				
			}
        }
  }


}
