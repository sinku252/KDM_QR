package com.Mayank.MML;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemDetails extends AppCompatActivity
{
    private ListView listView;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    private MyDatabase db;
    EmployeeListAdapter employeeListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details_main);
        db = MyApplication.getMyDatabase();
        listView = findViewById(R.id.listview_data);
        showData();

    }
    public void showData() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final List<Employee> userList = db.employeeDao().getAllEmployees(getIntent().getStringExtra("name"));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        employeeListAdapter = new EmployeeListAdapter(ItemDetails.this, userList);
                        listView.setAdapter(employeeListAdapter);
                    }
                });
            }
        });
    }
}
