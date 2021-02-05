package com.example.user.specialinformation;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView_startInfo;
    ArrayList<UserModel> myList;
    FloatingActionButton fab;
    static DatabaseHelper databaseHelper;
    RecyclerView recyclerView_project;
    RecyclerViewAdapter recyclerViewAdapter;
    Button button_reload;
    SwipeRefreshLayout swipeRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        textView_startInfo = findViewById(R.id.textView_info);
        fab = findViewById(R.id.fab);
        swipeRefreshLayout = findViewById(R.id.swipe_up_refresh);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                showList();

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        databaseHelper = new DatabaseHelper(this);

        recyclerView_project = findViewById(R.id.recyclerView);
        recyclerView_project.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_project.setHasFixedSize(true);



        textView_startInfo.setText("Xoş gəlmisiniz ! aşağıdakı  '+' buttonuna basaraq yeni ad əlavə edə bilərsiniz");

        showList();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this,AddUser.class));
            }
        });


    }



    private  void showList()
    {
        myList = new ArrayList<UserModel>();

        Cursor cursor = databaseHelper.getData();
        while (cursor.moveToNext())
        {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            String adress = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            myList.add(new UserModel(id,name, phone,adress,image));
        }

        // create new costomAdapter
       recyclerViewAdapter = new RecyclerViewAdapter(this,myList);
       recyclerView_project.setAdapter(recyclerViewAdapter);
    }


}
