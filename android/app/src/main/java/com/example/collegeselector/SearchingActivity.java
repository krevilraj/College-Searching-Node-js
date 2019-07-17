package com.example.collegeselector;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.collegeselector.adapter.CollegesAdapter;
import com.example.collegeselector.helper.CollegeAPI;
import com.example.collegeselector.helper.Helper;
import com.example.collegeselector.model.CollegeModel;
import com.example.collegeselector.model.Data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchingActivity extends AppCompatActivity {

    private ImageView mImgBack;
    private EditText mEdtSearch;
    private ImageView mImgSearch;
    private RecyclerView mRecyclerview;

    private RecyclerView recyclerView;
    LinearLayoutManager manager;
    CollegesAdapter adapter;

    List<Data> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        mImgBack = findViewById(R.id.img_back);
        mEdtSearch = findViewById(R.id.edt_search);
        mImgSearch = findViewById(R.id.img_search);
        mRecyclerview = findViewById(R.id.recyclerview);
        mImgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEdtSearch.getText().toString())) {
                    mEdtSearch.setError("Must write any college name");
                    mEdtSearch.requestFocus();
                } else {
                    search();
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        manager = new LinearLayoutManager(this);
        adapter = new CollegesAdapter(this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }


    private void search() {

        String query = mEdtSearch.getText().toString();

        String url = CollegeAPI.base_url + "college_search_json";
        Helper.logme(url,query);
        final Call<CollegeModel> userInfo = CollegeAPI.getService().search_data(url, query);
        userInfo.enqueue(new Callback<CollegeModel>() {
            @Override
            public void onResponse(Call<CollegeModel> call, Response<CollegeModel> response) {
                if (response.body() != null) {
                    CollegeModel item = response.body();
                    List<Data> s_items = item.getData();
                    adapter.clear_data();
                    items.addAll(s_items);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(SearchingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SearchingActivity.this, "Try another query", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<CollegeModel> call, Throwable t) {
                Toast.makeText(SearchingActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }
}
