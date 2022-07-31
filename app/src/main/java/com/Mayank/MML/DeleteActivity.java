package com.Mayank.MML;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity {

    private ListView listView;
    private EditText searchInput;
    private EmployeeListAdapter employeeListAdapter;
    private MyDatabase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        db = MyApplication.getMyDatabase();

        listView = findViewById(R.id.employeeListView);
        searchInput = findViewById(R.id.etSearchInput);
        new LoadAllProducts().execute();




        }

    class LoadAllProducts extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            try {
                db.clearAllTables();
                Intent intent=new Intent(DeleteActivity.this,CenterAudit.class);
                startActivity(intent);
                finish();

            }
            catch (Exception e) {
                Toast.makeText(DeleteActivity.this, "Exception" + e.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DeleteActivity.this, CenterAudit.class);
        //Intent intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    }