package com.example.collegeselector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.collegeselector.adapter.CollegesAdapter;
import com.example.collegeselector.helper.CollegeAPI;
import com.example.collegeselector.model.CollegeModel;
import com.example.collegeselector.model.Data;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
    private LinearLayout lnr_college_add,lnr_home,lnr_search,lnr_profile,lnr_logout;

    private RecyclerView recyclerView;
    LinearLayoutManager manager;
    CollegesAdapter adapter;

    List<Data> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        lnr_home=findViewById(R.id.lnr_support);
        lnr_search=findViewById(R.id.lnr_search);
        lnr_college_add=findViewById(R.id.lnr_add);
        lnr_profile=findViewById(R.id.lnr_profile);
        lnr_logout=findViewById(R.id.lnr_logout);

        recyclerView=findViewById(R.id.recyclerview);
        manager = new LinearLayoutManager(this);
        adapter = new CollegesAdapter(this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        getData();

        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DashboardActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
        lnr_college_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DashboardActivity.this, CollegeAddActivity.class);
                startActivity(intent);
            }
        });
        lnr_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DashboardActivity.this, SearchingActivity.class);
                startActivity(intent);

            }
        });
        lnr_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(DashboardActivity.this, EditProfileActivity.class);
                startActivity(intent);

            }
        });
        lnr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("UserDetail", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username", "");
                editor.putString("token", "");
                editor.putString("user_id", "");
                editor.apply();
                Toast.makeText(DashboardActivity.this, "You are logout!!!", Toast.LENGTH_SHORT).show();

                Intent intent =new Intent(DashboardActivity.this, MainActivity.class);
                                startActivity(intent);

            }
        });


    }

    private void getData() {

        final Call<CollegeModel> postList = CollegeAPI.getService().getCollege();
        postList.enqueue(new Callback<CollegeModel>() {
            @Override
            public void onResponse(Call<CollegeModel> call, Response<CollegeModel> response) {
                if(response.body()!=null) {
                    CollegeModel item = response.body();
                    List<Data> s_items = item.getData();
                    items.addAll(s_items);
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(DashboardActivity.this, "Some error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CollegeModel> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });

    }
}
